package com.flcat.stock_market;

import com.flcat.stock_market.entity.MarketPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<MarketPrice, Long> {
    Optional<MarketPrice> findByTicker(String ticker);
    Page<MarketPrice> findByTickerContaining(String ticker, Pageable pageable);
}