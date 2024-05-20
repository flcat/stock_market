package com.flcat.stock_market.service;

import com.flcat.stock_market.dto.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackService {

    private final String webhookUrl;

    public SlackService(@Value("${webhook.slack.url}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void sendStockPriceToSlack(List<Stock> stockList) {
        StringBuilder message = new StringBuilder();
        for (Stock stock : stockList) {
            message.append("Open: ").append(stock.getOpen()).append("\n");
            message.append("High: ").append(stock.getHigh()).append("\n");
            message.append("Low: ").append(stock.getLow()).append("\n");
            message.append("Close: ").append(stock.getClose()).append("\n");
            message.append("Volume: ").append(stock.getVolume()).append("\n");
            message.append("\n");
        }

        sendMessageToSlack("Overseas Stock Price", message.toString());
    }

    public void sendMessage(String title, HashMap<String, String> data) {
        StringBuilder message = new StringBuilder();
        message.append(title).append("\n");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sendMessageToSlack(title, message.toString());
    }

    private void sendMessageToSlack(String title, String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("text", title + "\n" + message);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        restTemplate.postForObject(webhookUrl, entity, String.class);
    }
}