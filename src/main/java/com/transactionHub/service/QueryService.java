package com.transactionHub.service;

import com.transactionHub.util.helper.TransactionTranslator;
import com.transactionHub.repository.TransactionRepository;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class QueryService {

    @Inject
    TransactionRepository repository;

    public List<Transaction> findTransactionsByDate(Date start, Date end, AccountEnum account) {
        return repository.findTransactionsByDate(start, end, account)
                .stream()
                .map(TransactionTranslator::mapToDomain)
                .sorted()
                .toList();
    }
}
