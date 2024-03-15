package com.flcat.stock_market.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenCreateRes {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("access_token_expired")
    private String expired;
    @JsonProperty("token_type")
    private String type;
    @JsonProperty("expires_in")
    private long expiresIn;
}
