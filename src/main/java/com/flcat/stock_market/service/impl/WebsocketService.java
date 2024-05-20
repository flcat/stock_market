package com.flcat.stock_market.service.impl;

import com.flcat.stock_market.exception.FailedResponseException;
import com.flcat.stock_market.util.AuthenticationManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebsocketService {

    private final AuthenticationManager authenticationManager;

    private String approvalKey;

    @PostConstruct
    protected void init() throws IOException, InterruptedException, FailedResponseException {
        log.info("[init] ApprovalKey 초기화 시작");
        this.authenticationManager.createAccessKey();
        scheduleAccessKeyRefresh();
        log.info("[init] ApprovalKey 초기화 완료");
    }

    private void scheduleAccessKeyRefresh() {
        TimerTask timerTask = new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                try {
                    approvalKey = authenticationManager.getAccessKey();
                    System.out.println(approvalKey);
                } catch (Exception e) {
                    log.error("Error refreshing access key", e);
                }
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long interval = 12 * 60 * 60 * 1000;

        timer.scheduleAtFixedRate(timerTask, delay, interval);
    }
}