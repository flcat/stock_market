package com.flcat.stock_market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverseasPriceDto {
    private String rt_cd;
    private String msg_cd;
    private String msg1;
    private Object output;
    private String rsym;
    private String zdiv;
    private String base;
    private String pvol;
    private String last;
    private String sign;
    private String diff;
    private String rate;
    private String tvol;
    private String tamt;
    private String ordy;
}