package com.flcat.stock_market.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PriceService {
    private final Logger logger = LoggerFactory.getLogger(PriceService.class);

    @Transactional
    public CompletableFuture<List<Map<String, Object>>> getMarketPriceFromPython(int page, int size) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Python 스크립트 실행
                executeMarketPriceScript();

                // 티커 정보가 담긴 CSV 파일 경로
                String csvFilePath = "/Users/jaechankwon/python_workspace/stock_list/Wilshire-20-Stocks.csv";

                // CSV 파일 읽기
                List<String> tickers = readTickersFromCsv(csvFilePath);

                // 결과 저장할 리스트
                List<Map<String, Object>> resultList = new ArrayList<>();

                // 각 티커에 대한 JSON 파일 읽기
                for (String ticker : tickers) {
                    String jsonFilePath = "/Users/jaechankwon/python_workspace/stock_list/" + ticker + ".json";
                    if (Files.exists(Paths.get(jsonFilePath))) {
                        List<Map<String, Object>> tickerResultList = readJsonFile(jsonFilePath);
                        resultList.addAll(tickerResultList);
                    } else {
                        log.warn("JSON file not found for ticker: {}", ticker);
                    }
                }

                // 페이지네이션 적용
                int start = (page - 1) * size;
                int end = Math.min(start + size, resultList.size());
                return resultList.subList(start, end);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read result files", e);
            }
        });
    }

    // CSV 파일에서 티커 정보 읽어오기
    private List<String> readTickersFromCsv(String csvFilePath) throws IOException {
        List<String> tickers = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            if (values.length > 0) {
                tickers.add(values[0]);
            }
        }
        reader.close();
        return tickers;
    }

    // JSON 파일 읽어서 List<Map<String, Object>>로 변환
    private List<Map<String, Object>> readJsonFile(String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(jsonFilePath), new TypeReference<List<Map<String, Object>>>() {});
    }
    public void executeMarketPriceScript() {
        try {
            String scriptPath = "/Users/jaechankwon/python_workspace/stock_list/nasdaq100_data_crawling.py";

            String[] cmd = new String[2];
            cmd[0] = "python3";
            cmd[1] = scriptPath;

            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process process = pb.start();

            // 프로세스의 출력 스트림 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // 에러 스트림 읽기
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    System.err.println(errorLine);
                }
                throw new RuntimeException("Python script exited with code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute Python script", e);
        }
    }

    public int getTotalElements() {
        try {
            // 티커 정보가 담긴 CSV 파일 경로
            String csvFilePath = "/Users/jaechankwon/python_workspace/stock_list/Wilshire-20-Stocks.csv";

            // CSV 파일 읽기
            List<String> tickers = readTickersFromCsv(csvFilePath);

            int totalElements = 0;

            // 각 티커에 대한 JSON 파일 읽기
            for (String ticker : tickers) {
                String jsonFilePath = "/Users/jaechankwon/python_workspace/stock_list/" + ticker + ".json";
                if (Files.exists(Paths.get(jsonFilePath))) {
                    List<Map<String, Object>> tickerResultList = readJsonFile(jsonFilePath);
                    totalElements += tickerResultList.size();
                } else {
                    log.warn("JSON file not found for ticker: {}", ticker);
                }
            }

            return totalElements;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read result files", e);
        }
    }

}