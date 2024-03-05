package com.flcat.stock_market.vo.tttt1002u;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TTTT1002UOrder {
    @JsonSetter("CANO")
    private String CANO;
    @JsonSetter("ACNT_PRDT_CD")
    private String ACNT_PRDT_CD;
    @JsonSetter("OVRS_EXCG_CD")
    private String OVRS_EXCG_CD;
    @JsonSetter("PDNO")
    private String PDNO;
    @JsonSetter("ORD_QTY")
    private String ORD_QTY;
    @JsonSetter("OVRS_ORD_UNPR")
    private String OVRS_ORD_UNPR;
    @JsonSetter("CTAC_TLNO")
    private String CTAC_TLNO;
    @JsonSetter("MGCO_APTM_ODNO")
    private String MGCO_APTM_ODNO;
    @JsonSetter("SLL_TYPE")
    private String SLL_TYPE;
    @JsonSetter("ORD_SVR_DVSN_CD")
    private String ORD_SVR_DVSN_CD;
    @JsonSetter("ORD_DVSN")
    private String ORD_DVSN;
}
