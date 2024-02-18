package com.flcat.stock_market.repository;

import com.flcat.stock_market.entity.StocksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveStocksStatusRepository extends JpaRepository<StocksEntity, Long> {
}
