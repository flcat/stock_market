package com.flcat.stock_market.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;


@SpringBootTest
class SlackServiceTest {
    @Autowired
    SlackService slackService;

    @Test
    @DisplayName("Slack 메세지 전송")
    void sendMessage() {
        String title = "Slack 메세지 전송 테스트";
        HashMap<String, String> data = new HashMap<>();
        data.put("test1", "test1 content");
        data.put("test2", "test2 content");

        slackService.sendMessage(title, data);
    }

}