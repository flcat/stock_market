package com.flcat.stock_market;

import com.flcat.stock_market.entity.StockPriceData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockPriceDataRepository extends JpaRepository<StockPriceData, Long> {
    List<StockPriceData> findByTicker(String ticker);
    List<StockPriceData> findByTickerAndDateBetween(String ticker, LocalDate startDate, LocalDate endDate);
    List<StockPriceData> findByDateBetween(LocalDate startDate, LocalDate endDate);
}