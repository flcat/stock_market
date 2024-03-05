package com.flcat.stock_market.controller;

import com.flcat.stock_market.service.impl.StocksBalanceService;
import com.flcat.stock_market.service.impl.StocksOrderService;
import com.flcat.stock_market.service.impl.WebsocketService;
import com.flcat.stock_market.util.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class LiveStocksController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private StocksBalanceService stocksBalanceService;
    @Autowired
    private WebsocketService websocketService;
    @Autowired
    private StocksOrderService orderService;

    @GetMapping("/createToken")
    @Scheduled(cron = "${job.cron.ki.createToken}")
    public void createToken() {
        long startTime = System.currentTimeMillis();
        try {
            this.startedInfoLog("createToken");
            this.authenticationManager.createToken();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            this.finishedInfoLog("createToken", startTime);
        }
    }
    @GetMapping("/get/balance")
    @Scheduled(cron = "${job.cron.ki.stocksBalance}")
    public void getBalance() throws IOException, JSONException {
        long startTime = System.currentTimeMillis();
        try {
            this.startedInfoLog("stocksBalance");
            this.stocksBalanceService.execute();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            this.finishedInfoLog("stocksBalance", startTime);
        }
    }
    @PostMapping("/order")
    @Scheduled(cron = "${job.cron.ki.order}")
    public void getOrder() throws IOException, JSONException {
        long startTime = System.currentTimeMillis();
        try {
            this.startedInfoLog("order");
            this.orderService.execute();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            this.finishedInfoLog("order", startTime);
        }
    }
    @MessageMapping("/queue/market_price")
//    @Scheduled(cron = "${job.cron.ki.market_price}")
    public void getMarketPrice() {
        long startTime = System.currentTimeMillis();
        try {
            this.startedInfoLog("marketPrice");
            this.websocketService.doSendJson();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            this.finishedInfoLog("marketPrice", startTime);
        }
    }

    private void startedInfoLog(String methodName) {
        if (log.isInfoEnabled()) {
            log.info("{} started.", methodName);
        }
    }

    private void finishedInfoLog(String methodName, long startTime) {
        if (log.isInfoEnabled()) {
            log.info("{} finished. elapsed time : {} ms", methodName, (System.currentTimeMillis()- startTime));
        }
    }
}
