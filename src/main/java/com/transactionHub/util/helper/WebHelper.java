package com.transactionHub.util.helper;

import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import jakarta.ws.rs.WebApplicationException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.Instant;
import java.util.Date;

public class WebHelper {

    public static AccountEnum parseAccountEnum(String inputEnum) {
        try {
            return AccountEnum.valueOf(inputEnum);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new WebApplicationException(String.format("invalid account enum %s", inputEnum));
        }
    }

    public static Instant convertToInstant(Date date) {
        DateTime tmp = new DateTime(date.getTime());
        long millis = new DateTime(tmp.getYear(), tmp.getMonthOfYear(), tmp.getDayOfMonth(), 0, 0, DateTimeZone.UTC).getMillis();
        return Instant.ofEpochMilli(millis);
    }

}
