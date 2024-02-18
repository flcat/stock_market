package com.flcat.stock_market.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndexDataEntity {

    private String rt_cd;
    private String msg_cd;
    private String message;
    private Object output1;
    private Object[] output2;

    public IndexDataEntity(String rt_cd, String msg_cd, String message, Object output1, Object[] output2) {
        this.rt_cd = rt_cd;
        this.msg_cd = msg_cd;
        this.message = message;
        this.output1 = output1;
        this.output2 = output2;
    }
}
