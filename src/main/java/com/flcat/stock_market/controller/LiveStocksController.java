package com.flcat.stock_market.controller;

import com.flcat.stock_market.service.impl.StocksBalanceService;
import com.flcat.stock_market.util.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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
