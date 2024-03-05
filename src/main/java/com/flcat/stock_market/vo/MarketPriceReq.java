package com.flcat.stock_market.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPriceReq {

    @JsonProperty("tr_id")
    private String tr_id;

    @JsonProperty("tr_key")
    private String tr_key;

    public MarketPriceReq(String tr_id, String tr_key) {
        this.tr_id = tr_id;
        this.tr_key = tr_key;
    }
}
