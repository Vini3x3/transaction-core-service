package com.transactionHub.util.config;

import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import io.smallrye.config.ConfigMapping;

import java.util.Map;
import java.util.Set;

@ConfigMapping(prefix = "import")
public interface ImportConfig {
    Map<AccountEnum, Pipeline> account();

    interface Pipeline {
        Map<String, Set<String>> taggerConfig();

        MapperConfig mapperConfig();
    }

    interface MapperConfig {
        String dateHeader();
        String descriptionHeader();
        String withdrawalHeader();
        String depositHeader();
        String balanceHeader();
        AccountEnum account();
        String datePattern();
    }

}
