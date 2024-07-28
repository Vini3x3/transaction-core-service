package com.transactionHub.service;

import com.transactionHub.repository.TransactionRepository;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.constant.TagConstant;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import com.transactionHub.transactionProcessor.extractor.Extractor;
import com.transactionHub.transactionProcessor.extractor.ExtractorFactory;
import com.transactionHub.transactionProcessor.mapper.transaction.TransactionMapper;
import com.transactionHub.transactionProcessor.modifier.SystemTagger;
import com.transactionHub.transactionProcessor.modifier.Tagger;
import com.transactionHub.transactionProcessor.pipeline.ImportPipeline;
import com.transactionHub.transactionProcessor.pipeline.MergePipeline;
import com.transactionHub.util.config.ImportConfig;
import com.transactionHub.util.helper.TransactionTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;


@ApplicationScoped
public class ImportService {

    @Inject
    TransactionRepository repository;

    @Inject
    ImportConfig importConfig;

    public void importData(AccountEnum account, String fileType, String filename, InputStream inputStream) {
        var importTransactions = parseData(inputStream, filename, fileType, importConfig.account().get(account));
        var mergedTransactions = mergeVirtualTransaction(importTransactions);

        var entities = mergedTransactions.stream()
                .map(TransactionTranslator::mapToEntity).toList();

        repository.persistOrUpdate(entities);
    }

    protected List<Transaction> parseData(InputStream inputStream, String filename, String fileType, ImportConfig.Pipeline pipelineConfig) {
        ImportConfig.ExtractorConfig extractorConfig = pipelineConfig.extractorConfig();
        Map<String, Set<String>> taggerConfig = pipelineConfig.taggerConfig();
        ImportConfig.MapperConfig mapperConfig = pipelineConfig.mapperConfig();
        var mapper = new TransactionMapper(
                mapperConfig.dateHeader(),
                mapperConfig.descriptionHeader(),
                mapperConfig.withdrawalHeader(),
                mapperConfig.depositHeader(),
                mapperConfig.deltaHeader().orElse("NOT SET"),
                mapperConfig.balanceHeader(),
                mapperConfig.account(),
                mapperConfig.datePattern()
        );
        var extractor = createExtractor(extractorConfig, fileType);
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
        Instant startDate = importTransactions.getFirst().getDate();
        Instant endDate = importTransactions.getLast().getDate();

        var existingTransactions = repository.findTransactionsByDate(startDate, endDate, account)
                .stream()
                .map(TransactionTranslator::mapToDomain)
                .toList();

        if (existingTransactions.isEmpty()) {
            return importTransactions;
        }

        var toBeDeleted = existingTransactions.stream().filter(o -> {
            for (var t : importTransactions) {
                if (o.getDate().equals(t.getDate()) & o.getOffset().equals(t.getOffset()) && o.getAccount().equals(t.getAccount())) {
                    return false;
                }
            }
            return true;
        }).toList();

        for (var t : toBeDeleted) {
            repository.delete(TransactionTranslator.mapToEntity(t));
        }

        var virtualTransactions = existingTransactions.stream().filter(o -> o.getTags().contains(TagConstant.VIRTUAL)).toList();

        return new MergePipeline().mergeData(importTransactions, virtualTransactions);
    }

    private Extractor createExtractor(ImportConfig.ExtractorConfig extractorConfig, String fileType) {
        var delimiter = extractorConfig.delimiter();
        return delimiter.map(character -> ExtractorFactory.create(fileType, character)).orElseGet(() -> ExtractorFactory.create(fileType));
    }


}
