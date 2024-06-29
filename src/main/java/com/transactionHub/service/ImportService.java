package com.transactionHub.service;

import com.transactionHub.entity.TransactionTranslator;
import com.transactionHub.repository.TransactionRepository;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.constant.TagConstant;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import com.transactionHub.transactionProcessor.extractor.Extractor;
import com.transactionHub.transactionProcessor.extractor.csv.CsvExtractor;
import com.transactionHub.transactionProcessor.extractor.excel.ExcelExtractor;
import com.transactionHub.transactionProcessor.mapper.transaction.TransactionMapper;
import com.transactionHub.transactionProcessor.modifier.SystemTagger;
import com.transactionHub.transactionProcessor.modifier.Tagger;
import com.transactionHub.transactionProcessor.pipeline.ImportPipeline;
import com.transactionHub.transactionProcessor.pipeline.MergePipeline;
import com.transactionHub.util.config.ImportConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.util.*;


@ApplicationScoped
public class ImportService {

    @Inject
    TransactionRepository repository;

    @Inject
    ImportConfig importConfig;

    public void importCsv(AccountEnum account, InputStream inputStream, String filename) {
        var extractor = new CsvExtractor();
        importData(inputStream, filename, extractor, account);
    }

    public void importExcel(AccountEnum accountEnum, InputStream inputStream, String filename) {
        var extractor = new ExcelExtractor();
        importData(inputStream, filename, extractor, accountEnum);
    }

    public void importData(InputStream inputStream, String filename, Extractor extractor, AccountEnum account) {
        var importTransactions = parseData(inputStream, filename, extractor, importConfig.account().get(account));
        var mergedTransactions = mergeVirtualTransaction(importTransactions);

        var entities = mergedTransactions.stream()
                .map(TransactionTranslator::mapToEntity).toList();

        repository.persistOrUpdate(entities);

    }

    protected List<Transaction> parseData(InputStream inputStream, String filename, Extractor extractor, ImportConfig.Pipeline pipelineConfig) {
        Map<String, Set<String>> taggerConfig = pipelineConfig.taggerConfig();
        ImportConfig.MapperConfig mapperConfig = pipelineConfig.mapperConfig();
        var mapper = new TransactionMapper(
                mapperConfig.dateHeader(),
                mapperConfig.descriptionHeader(),
                mapperConfig.withdrawalHeader(),
                mapperConfig.depositHeader(),
                mapperConfig.balanceHeader(),
                mapperConfig.account(),
                mapperConfig.datePattern()
        );
        var tagger = new Tagger(taggerConfig);
        var systemTagger = new SystemTagger(pipelineConfig.systemTaggerConfig());
        var importPipeline = new ImportPipeline(extractor, mapper, tagger, systemTagger);
        return importPipeline.importData(inputStream, filename);
    }

    protected List<Transaction> mergeVirtualTransaction(List<Transaction> importTransactions) {
        if (importTransactions.isEmpty()) {
            return importTransactions;
        }

        AccountEnum account = importTransactions.getFirst().getAccount();
        Date startDate = importTransactions.getFirst().getDate();
        Date endDate = importTransactions.getLast().getDate();

        var existingTransactions = repository.findTransactionsByDate(startDate, endDate, account)
                .stream()
                .map(TransactionTranslator::mapToDomain)
                .toList();

        if (existingTransactions.isEmpty()) {
            return importTransactions;
        }

        var toBeDeleted = existingTransactions.stream().filter(o -> {
            for (var t: importTransactions) {
                if (o.getDate().equals(t.getDate()) & o.getOffset().equals(t.getOffset()) && o.getAccount().equals(t.getAccount())) {
                    return false;
                }
            }
            return true;
        }).toList();

        for (var t: toBeDeleted) {
            repository.delete(TransactionTranslator.mapToEntity(t));
        }

        var virtualTransactions = existingTransactions.stream().filter(o -> o.getTags().contains(TagConstant.VIRTUAL)).toList();

        return new MergePipeline().mergeData(importTransactions, virtualTransactions);
    }

}
