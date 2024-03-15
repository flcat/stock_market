package com.flcat.stock_market.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalKeyRes {

    @JsonProperty("approval_key")
    private String approval_key;
}
