package com.flcat.stock_market.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flcat.stock_market.config.KisConfig;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenCreateReq {

    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("appkey")
    private String appKey;

    @JsonProperty("appsecret")
    private String appSecret;

    public TokenCreateReq(KisConfig kisConfig) {
        this.appKey = kisConfig.getAPPKEY();
        this.appSecret = kisConfig.getAPPSECRET();
        this.grantType = kisConfig.getGrantType();
    }
}
