package com.flcat.stock_market.service;

import com.flcat.stock_market.dto.StockDto;
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

    public void sendStockPriceToSlack(List<StockDto> stockDtoList) {
        StringBuilder message = new StringBuilder();
        for (StockDto stockDto : stockDtoList) {
            message.append("Open: ").append(stockDto.getOpen()).append("\n");
            message.append("High: ").append(stockDto.getHigh()).append("\n");
            message.append("Low: ").append(stockDto.getLow()).append("\n");
            message.append("Close: ").append(stockDto.getClose()).append("\n");
            message.append("Adj Close: ").append(stockDto.getAdjClose()).append("\n");
            message.append("Volume: ").append(stockDto.getVolume()).append("\n");
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