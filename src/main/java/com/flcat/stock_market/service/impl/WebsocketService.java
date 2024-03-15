package com.flcat.stock_market.service.impl;

import com.flcat.stock_market.exception.FailedResponseException;
import com.flcat.stock_market.service.SlackService;
import com.flcat.stock_market.util.AuthenticationManager;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class WebsocketService {

    @Autowired
    private SlackService slackService;
    @Autowired
    private AuthenticationManager authenticationManager;


    private String approvalKey;
    private String custType;
    private String contentType;
    private String uri;
    private String port;
    private static boolean bContinue = true;

    public Properties readProperties(String propFileName) {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        try {
            if (inputStream != null) {
                prop.load(inputStream);
                return prop;
            } else {
                throw new FileNotFoundException("Properties file " + propFileName + " 을 resource에서 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    static WebsocketClientEndpoint clientEndpoint;

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
                    throw new RuntimeException(e);
                }

            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long interval = 12 * 60 * 60 * 1000;

        timer.scheduleAtFixedRate(timerTask, delay, interval);
    }

    public void doSendJson() {
        Properties properties = readProperties("ovrs.properties");

        uri = properties.getProperty("url");
        port = properties.getProperty("port");
        custType = properties.getProperty("custType");
        contentType = properties.getProperty("contentType");

//            clientEndpoint = new WebsocketClientEndpoint(new URI(uri+":"+port+"/tryitout/HDFSASP0"));
//
//            clientEndpoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
//                @Override
//                public void handleMessage(String message) {
//                    System.out.println(message);
//                }
//            });

    }

    private void showMessage(String stockCode) {
    }

    private void sendMessageToUser(String message) {
        String title = "========================";
        HashMap<String, String> data = new HashMap<>();
        data.put("시세 정보", message);
        slackService.sendMessage(title,data);
    }
}
