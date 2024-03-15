package com.flcat.stock_market.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flcat.stock_market.config.KisConfig;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalKeyReq {
    @JsonProperty("appkey")
    private String appKey;
    @JsonProperty("secretkey")
    private String appSecret;
    public ApprovalKeyReq(KisConfig kisConfig) {
        this.appKey = kisConfig.getAPPKEY();
        this.appSecret = kisConfig.getAPPSECRET();
    }
}
