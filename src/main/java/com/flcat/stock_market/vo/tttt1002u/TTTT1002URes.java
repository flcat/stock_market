package com.flcat.stock_market.vo.tttt1002u;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TTTT1002URes {
    private String tr_cd;
    private String msg_cd;
    private String msg1;
    private String ctx_area_fk200;
    private String ctx_area_nk200;
    private List<TTTT1002UOrder> output;
    private List<TTTT1002UBalanceDTO> output1;
    private String KRX_FWDG_ORD_ORGNO;
    private String ODNO;
    private String ORD_TMD;
    private Object output2;
}
