package com.flcat.stock_market.service;

import com.flcat.stock_market.entity.BalanceEntity;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UBalanceDTO;
import org.json.JSONException;

import java.io.IOException;


public interface LiveStocksWebSocketService {
    String getStocks() throws IOException, JSONException;

    BalanceEntity getBalance(BalanceEntity responseDto) throws IOException, JSONException;
}
