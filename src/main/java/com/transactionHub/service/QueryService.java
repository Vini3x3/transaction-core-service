package com.transactionHub.service;

import com.transactionHub.repository.TransactionRepository;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import com.transactionHub.util.helper.TransactionTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class QueryService {

    @Inject
    TransactionRepository repository;

    public List<Transaction> findTransactionsByDate(Instant start, Instant end, AccountEnum account) {
        return repository.findTransactionsByDate(start, end, account)
                .stream()
                .map(TransactionTranslator::mapToDomain)
                .sorted()
                .toList();
    }

    public Transaction findTransactionById(Instant date, int offset, AccountEnum account) {
        return TransactionTranslator.mapToDomain(repository.findById(date, offset, account));
    }
}
