package com.flcat.stock_market.util;

import java.util.UUID;

public class KiRequestHelper {

    private KiRequestHelper() {
        throw new IllegalStateException("Utility Class");
    }

    public static String makeGtUid() {
        return DateHelper.nowStr() + "-" + UUID.randomUUID().toString().substring(22);
    }
}
