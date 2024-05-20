package com.flcat.stock_market.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();

    public DateHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static synchronized long stringToLong(String format, String dateTimeStr) throws ParseException {
        DATE_FORMAT.applyPattern(format);
        return DATE_FORMAT.parse(dateTimeStr).getTime();
    }

    public static synchronized String nowStr() {
        DATE_FORMAT.applyPattern("yyyyMMddHHmmssSSS");
        return DATE_FORMAT.format(new Date());
    }
}
