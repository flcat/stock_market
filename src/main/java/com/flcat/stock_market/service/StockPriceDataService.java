package com.flcat.stock_market.service;

import com.flcat.stock_market.StockPriceDataRepository;
import com.flcat.stock_market.entity.StockPriceData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockPriceDataService {
    private final StockPriceDataRepository stockPriceDataRepository;

    @Transactional
    public void saveStockPriceDataFromJsonFile(String fileName) {
        try {
            String filePath = "python_workspace/stock_list/" + fileName;
            JsonArray jsonArray = JsonParser.parseReader(new FileReader(filePath)).getAsJsonArray();

            List<StockPriceData> stockDataList = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();

                StockPriceData stockPriceData = new StockPriceData();
                stockPriceData.setTicker(jsonObject.get("ticker").getAsString());
                stockPriceData.setDate(LocalDate.parse(jsonObject.get("date").getAsString()));
                stockPriceData.setOpen(jsonObject.get("open").getAsBigDecimal());
                stockPriceData.setHigh(jsonObject.get("high").getAsBigDecimal());
                stockPriceData.setLow(jsonObject.get("low").getAsBigDecimal());
                stockPriceData.setClose(jsonObject.get("close").getAsBigDecimal());
                stockPriceData.setVolume(jsonObject.get("volume").getAsBigDecimal());

                stockDataList.add(stockPriceData);
            }

            stockPriceDataRepository.saveAll(stockDataList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stock data from JSON file", e);
        }
    }

    @Transactional(readOnly = true)
    public List<StockPriceData> getAllStockData() {
        return stockPriceDataRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<StockPriceData> getStockDataByTicker(String ticker) {
        return stockPriceDataRepository.findByTicker(ticker);
    }
}