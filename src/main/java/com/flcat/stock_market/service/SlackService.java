package com.flcat.stock_market.service;

import com.flcat.stock_market.config.ColorConfig;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {

    @Value("${webhook.slack.url}")
    private String SLACK_WEBHOOK_URL;

    private final Slack slackClient = Slack.getInstance();

    public void sendMessage(String title, HashMap<String, String> data) {
        try {
            slackClient.send(SLACK_WEBHOOK_URL, payload(p -> p
                            .text(title)
                            .attachments(List.of(Attachment.builder().color(ColorConfig.GREEN.getCode())
                                    .fields(
                                            data.keySet().stream().map(key -> generateSlackField(key, data.get(key))).collect(Collectors.toList())
                                    ).build())))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
