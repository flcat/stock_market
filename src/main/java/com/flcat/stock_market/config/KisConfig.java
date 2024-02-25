package com.flcat.stock_market.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "ki")
@EnableConfigurationProperties
public class KisConfig {
    private String REST_BASE_URL = "https://openapi.koreainvestment.com:9443";
    private String WS_BASE_URL = "ws://ops.koreainvestment.com:21000";
    @Value("${app_key}")
    private String APPKEY; // your APPKEY
    @Value("${app_secret}")
    private String APPSECRET;  // your APPSECRET
    @Value("${account_no}")
    private String accountNo;

    public static final String FHKUP03500100_PATH = "/uapi/domestic-stock/v1/quotations/inquire-daily-indexchartprice";
    public static final String FHKST03030100_PATH = "/uapi/overseas-price/v1/quotations/inquire-daily-chartprice";

}
