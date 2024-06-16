package com.transactionHub.repository;

import com.transactionHub.entity.Transaction;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.List;


@ApplicationScoped
public class TransactionRepository implements PanacheMongoRepository<Transaction> {

    public List<Transaction> findTransactionByDate(Date start, Date end, AccountEnum account){
        return find("_id.date >= ?1 and _id.date <= ?2 and _id.account = ?3", start, end, account).stream().toList();
    }

}