package com.flcat.stock_market.util;

import com.flcat.stock_market.vo.common.ResponseVO;

public class ApiResponse<T> extends ResponseVO<T> {
    public static <T> ResponseVO<T> of(T body) {
        return ResponseVO.success(body);
    }
}
