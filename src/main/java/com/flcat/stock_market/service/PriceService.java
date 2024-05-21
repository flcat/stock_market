package com.flcat.stock_market.service;

import com.flcat.stock_market.dto.StockDto;
import com.flcat.stock_market.exception.InvalidJsonFormatException;
import com.flcat.stock_market.exception.MarketPriceException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PriceService {
    private final Logger logger = LoggerFactory.getLogger(PriceService.class);

    public Page<StockDto> getMarketPriceFromPython(String ticker, String search, Pageable pageable) throws IOException, InterruptedException {
        String result = executeMarketPriceScript(ticker, search);
        List<StockDto> stockDtoList = parseJsonResult(result);
        List<StockDto> filteredStockListDto = filterStockList(stockDtoList, search);

        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), filteredStockListDto.size()));
        List<StockDto> paginatedStockListDto = filteredStockListDto.subList(start, end);

        return new PageImpl<>(paginatedStockListDto, pageable, filteredStockListDto.size());
    }

    private String executeMarketPriceScript(String ticker, String search) throws IOException, InterruptedException {
//        String pythonScript = "/home/user/python_workspace/stock_list/nasdaq100_data_crawling.py";
        String pythonScript = "/Users/jaechankwon/python_workspace/stock_list/nasdaq100_data_crawling.py";
        if (ticker == null) {
            ticker = "";
        }

        if (search == null) {
            search = "";
        }

        String[] command = {"/usr/bin/python3", pythonScript, ticker, search};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8.name()))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString();
        } finally {
            boolean completed = process.waitFor(60, TimeUnit.SECONDS);
            if (!completed) {
                process.destroyForcibly();
                throw new RuntimeException("Python script timed out");
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new RuntimeException("Python script exited with code " + exitCode);
            }
        }
    }

    private List<StockDto> parseJsonResult(String result) {
        try {
            JsonElement jsonElement = JsonParser.parseString(result);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<StockDto> priceList = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();

                StockDto stockDto = StockDto.builder()
                        .date(LocalDate.parse(jsonObject.get("date").getAsString()))
                        .open(jsonObject.get("open").getAsJsonObject().getAsBigDecimal())
                        .high(jsonObject.get("high").getAsJsonObject().getAsBigDecimal())
                        .low(jsonObject.get("low").getAsJsonObject().getAsBigDecimal())
                        .close(jsonObject.get("close").getAsJsonObject().getAsBigDecimal())
                        .volume(jsonObject.get("volume").getAsJsonObject().getAsBigDecimal())
                        .build();
                priceList.add(stockDto);
            }

            return priceList;
        } catch (JsonSyntaxException e) {
            logger.warn("Invalid JSON format: [result: {}]", result);
            throw new InvalidJsonFormatException("Invalid JSON format in the script result.", e);
        } catch (Exception e) {
            logger.error("Failed to parse price: [result: {}]", result, e);
            throw new MarketPriceException("Failed to parse price", e);
        }
    }

    private List<StockDto> filterStockList(List<StockDto> stockDtoList, String search) {
        if (search.isEmpty()) {
            return stockDtoList;
        }
        return stockDtoList.stream()
                .filter(stockDto -> stockDto.toString().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }
}