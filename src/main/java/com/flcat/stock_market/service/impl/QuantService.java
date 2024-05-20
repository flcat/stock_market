package com.flcat.stock_market.service.impl;

import com.flcat.stock_market.StockDataRepository;
import com.flcat.stock_market.entity.StockData;
import com.flcat.stock_market.dto.TradeSignal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuantService {

    private final StockDataRepository stockDataRepository;

    public List<TradeSignal> calculateTradeSignals(String symbol) {
        List<StockData> stockData = stockDataRepository.findBySymbol(symbol);
        List<TradeSignal> tradeSignals = new ArrayList<>();

        boolean isHolding = false;
        double currentPrice = 0.0;
        double buyPrice = 0.0;

        for (StockData data : stockData) {
            if (!isHolding && data.getClosePrice() > data.getMovingAverage()) {
                isHolding = true;
                buyPrice = data.getClosePrice();
                tradeSignals.add(new TradeSignal(data.getDate(), "Buy", data.getClosePrice()));
            } else if (isHolding && data.getClosePrice() < data.getMovingAverage()) {
                isHolding = false;
                currentPrice = data.getClosePrice();
                tradeSignals.add(new TradeSignal(data.getDate(), "Sell", currentPrice));
            }
        }

        return tradeSignals;
    }
}