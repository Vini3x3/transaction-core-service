package com.transactionHub.service;

import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionProcessor.extractor.Extractor;
import com.transactionHub.transactionProcessor.extractor.csv.CsvExtractor;
import com.transactionHub.transactionProcessor.mapper.transaction.TransactionMapper;
import com.transactionHub.transactionProcessor.mapper.transaction.TransactionMapperConfig;
import com.transactionHub.transactionProcessor.modifier.Tagger;
import com.transactionHub.transactionProcessor.pipeline.ImportPipeline;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class ImportService {

    public void importCsv(AccountEnum accountEnum, InputStream inputStream, String filename) {


        var extractor = new CsvExtractor("\\|", System.lineSeparator());
        var mapperConfig = new TransactionMapperConfig(
                "Date",
                "Transaction Details",
                "Withdrawal",
                "Deposit",
                "Balance in Original Currency",
                accountEnum,
                "yyyy/MM/dd"
        );
        var mapper = new TransactionMapper(mapperConfig);

        var tagger = new Tagger(Map.of());


        var importPipeline = new ImportPipeline(extractor, mapper, tagger);

        var result = importPipeline.importData(inputStream, filename);

        System.out.println(result.size());


    }

    public void importExcel(AccountEnum accountEnum, InputStream inputStream, String filename) {



    }

    private ImportPipeline createPipeline(Extractor extractor, TransactionMapper mapper, Tagger tagger) {

        return new ImportPipeline(extractor, mapper, tagger);
    }

}
