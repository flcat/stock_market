package com.flcat.stock_market.service;

import com.flcat.stock_market.entity.StocksEntity;
import com.flcat.stock_market.repository.LiveStocksStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LiveStocksStatusService {

    private final LiveStocksStatusRepository liveStocksStatusRepository;

    public LiveStocksStatusService(LiveStocksStatusRepository liveStocksStatusRepository) {
        this.liveStocksStatusRepository = liveStocksStatusRepository;
    }

    public void saveStocks(StocksEntity stocks) {
        liveStocksStatusRepository.save(stocks);
    }
}
