package com.flcat.stock_market.service.impl;

import com.flcat.stock_market.StockDataRepository;
import com.flcat.stock_market.entity.StockData;
import com.flcat.stock_market.dto.TradeSignal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        BigDecimal currentPrice = BigDecimal.ZERO;
        BigDecimal buyPrice = BigDecimal.ZERO;

        for (StockData data : stockData) {
            BigDecimal closePrice = data.getClose();
            BigDecimal movingAverage = data.getMovingAverage();

            if (!isHolding && closePrice.compareTo(movingAverage) > 0) {
                isHolding = true;
                buyPrice = closePrice;
                tradeSignals.add(new TradeSignal(data.getDate(), "Buy", closePrice));
            } else if (isHolding && closePrice.compareTo(movingAverage) < 0) {
                isHolding = false;
                currentPrice = closePrice;
                tradeSignals.add(new TradeSignal(data.getDate(), "Sell", currentPrice));
            }
        }

        return tradeSignals;
    }
}