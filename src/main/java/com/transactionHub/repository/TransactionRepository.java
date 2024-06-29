package com.transactionHub.repository;

import com.transactionHub.entity.Transaction;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ApplicationScoped
public class TransactionRepository implements PanacheMongoRepository<Transaction> {

    public List<Transaction> findTransactionsByDate(Date start, Date end, AccountEnum account) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", start);
        params.put("endDate", end);
        params.put("account", account);

        String query = "{ $and: [ { '_id.date' : { $gte: :startDate } }, { '_id.date' : { $lte: :endDate } }, { '_id.account' : :account } ] }";

        return list(query, params);
    }
}