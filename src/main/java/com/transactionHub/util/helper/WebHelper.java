package com.transactionHub.util.helper;

import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import jakarta.ws.rs.WebApplicationException;

public class WebHelper {

    public static AccountEnum parseAccountEnum(String inputEnum) {
        try {
            return AccountEnum.valueOf(inputEnum);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new WebApplicationException(String.format("invalid account enum %s", inputEnum));
        }

    }
}
