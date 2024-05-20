package com.flcat.stock_market.service;

import com.flcat.stock_market.dto.OverseasPriceDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PriceService {
    private final Logger logger = LoggerFactory.getLogger(PriceService.class);

    public Page<OverseasPriceDto> getMarketPriceFromPython(String ticker, String search, Pageable pageable) throws IOException, InterruptedException {
        String result = executeMarketPriceScript(ticker, search);
        List<OverseasPriceDto> stockList = parseJsonResult(result);
        List<OverseasPriceDto> filteredStockList = filterStockList(stockList, search);

        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), filteredStockList.size()));
        List<OverseasPriceDto> paginatedStockList = filteredStockList.subList(start, end);

        return new PageImpl<>(paginatedStockList, pageable, filteredStockList.size());
    }

    private String executeMarketPriceScript(String ticker, String search) throws IOException, InterruptedException {
        String pythonScript = "/home/user/python_workspace/stock_list/nasdaq100_data_crawling.py";
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

    private List<OverseasPriceDto> parseJsonResult(String result) {
        try {
            JsonElement jsonElement = JsonParser.parseString(result);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<OverseasPriceDto> priceList = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                JsonObject outputObject = jsonObject.getAsJsonObject("output");

                OverseasPriceDto priceDto = OverseasPriceDto.builder()
                        .rt_cd(jsonObject.get("rt_cd").getAsString())
                        .msg_cd(jsonObject.get("msg_cd").getAsString())
                        .msg1(jsonObject.get("msg1").getAsString())
                        .rsym(outputObject.get("rsym").getAsString())
                        .zdiv(outputObject.get("zdiv").getAsString())
                        .base(outputObject.get("base").getAsString())
                        .pvol(outputObject.get("pvol").getAsString())
                        .last(String.valueOf(outputObject.get("last").getAsBigDecimal()))
                        .sign(outputObject.get("sign").getAsString())
                        .diff(String.valueOf(outputObject.get("diff").getAsBigDecimal()))
                        .rate(outputObject.get("rate").getAsString())
                        .tvol(outputObject.get("tvol").getAsString())
                        .tamt(outputObject.get("tamt").getAsString())
                        .ordy(outputObject.get("ordy").getAsString())
                        .build();
                priceList.add(priceDto);
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

    private List<OverseasPriceDto> filterStockList(List<OverseasPriceDto> stockList, String search) {
        if (search.isEmpty()) {
            return stockList;
        }
        return stockList.stream()
                .filter(stock -> stock.getRsym().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }
}