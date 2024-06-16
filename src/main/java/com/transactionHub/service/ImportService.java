package com.transactionHub.service;

import com.transactionHub.entity.TransactionTranslator;
import com.transactionHub.repository.TransactionRepository;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.constant.TagType;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import com.transactionHub.transactionProcessor.extractor.Extractor;
import com.transactionHub.transactionProcessor.extractor.csv.CsvExtractor;
import com.transactionHub.transactionProcessor.extractor.excel.ExcelExtractor;
import com.transactionHub.transactionProcessor.mapper.transaction.TransactionMapper;
import com.transactionHub.transactionProcessor.mapper.transaction.TransactionMapperConfig;
import com.transactionHub.transactionProcessor.modifier.Tagger;
import com.transactionHub.transactionProcessor.pipeline.ImportPipeline;
import com.transactionHub.transactionProcessor.pipeline.MergePipeline;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.util.*;


@ApplicationScoped
public class ImportService {

    @Inject
    TransactionRepository repository;

    public void importCsv(AccountEnum accountEnum, InputStream inputStream, String filename) {

        var extractor = new CsvExtractor();
        var mapperConfig = new TransactionMapperConfig(
                "Date",
                "Transaction Details",
                "Withdrawal",
                "Deposit",
                "Balance in Original Currency",
                accountEnum,
                "yyyy/MM/dd"
        );
        var taggerConfig = new HashMap<String, Set<String>>();

        importData(inputStream, filename, extractor, mapperConfig, taggerConfig);

    }

    public void importExcel(AccountEnum accountEnum, InputStream inputStream, String filename) {

        var extractor = new ExcelExtractor();
        var mapperConfig = new TransactionMapperConfig(
                "Date",
                "Transaction Details",
                "Withdrawal",
                "Deposit",
                "Balance in Original Currency",
                accountEnum,
                "yyyy/MM/dd"
        );
        var taggerConfig = new HashMap<String, Set<String>>();

        importData(inputStream, filename, extractor, mapperConfig, taggerConfig);

    }

    public void importData(InputStream inputStream, String filename, Extractor extractor, TransactionMapperConfig mapperConfig, Map<String, Set<String>> taggerConfig) {

        var mapper = new TransactionMapper(mapperConfig);
        var tagger = new Tagger(taggerConfig);
        var importPipeline = new ImportPipeline(extractor, mapper, tagger);
        var importTransactions = importPipeline.importData(inputStream, filename);

        var mergedTransactions = mergeVirtualTransaction(importTransactions);
        var entities = mergedTransactions.stream()
                .map(TransactionTranslator::mapToEntity).toList();

        repository.persistOrUpdate(entities);

    }

    protected List<Transaction> mergeVirtualTransaction(List<Transaction> importTransactions) {
        if (importTransactions.isEmpty()) {
            return importTransactions;
        }

        AccountEnum account = importTransactions.getFirst().getAccount();
        Date startDate = importTransactions.getFirst().getDate();
        Date endDate = importTransactions.getLast().getDate();

        var existingTransactions = repository.findTransactionByDate(startDate, endDate, account)
                .stream()
                .map(TransactionTranslator::mapToDomain)
                .toList();

        if (existingTransactions.isEmpty()) {
            return importTransactions;
        }

        var virtualTransactions = existingTransactions.stream().filter(o -> o.getTags().contains(TagType.VIRTUAL)).toList();

        return new MergePipeline().mergeData(importTransactions, virtualTransactions);
    }

}
