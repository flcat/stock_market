package com.flcat.stock_market.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BodyEntity {


    private String rt_cd;
    private String msg_cd;
    private String message;
    private Object output;

    public BodyEntity(String rt_cd, String msg_cd, String message, Object output) {
        this.rt_cd = rt_cd;
        this.msg_cd = msg_cd;
        this.message = message;
        this.output = output;
    }
}
