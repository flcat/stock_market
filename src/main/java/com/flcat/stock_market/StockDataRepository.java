package com.flcat.stock_market;

import com.flcat.stock_market.entity.StockData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockDataRepository extends JpaRepository<StockData, Long> {
    List<StockData> findBySymbol(String symbol);
}
