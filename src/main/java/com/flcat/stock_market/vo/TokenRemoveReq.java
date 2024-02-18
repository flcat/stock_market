package com.flcat.stock_market.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flcat.stock_market.config.KisConfig;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenRemoveReq {
    @JsonProperty("appkey")
    private String appKey;
    @JsonProperty("appsecret")
    private String appSecret;
    private String token;

    public TokenRemoveReq(KisConfig kisConfig) {
        this.appKey = kisConfig.getAPPKEY();
        this.appSecret = kisConfig.getAPPSECRET();
    }
}
