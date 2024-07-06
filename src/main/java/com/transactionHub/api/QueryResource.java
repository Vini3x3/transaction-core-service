package com.transactionHub.api;

import com.transactionHub.service.QueryService;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.Date;
import java.util.List;

@Path("/query")
public class QueryResource {

    @Inject
    QueryService queryService;

    @Path("date-range")
    @GET
    public List<Transaction> findByDateRange(@QueryParam("start") Date start, @QueryParam("end") Date end, @QueryParam("account") AccountEnum account) {
        return queryService.findTransactionsByDate(start.toInstant(), end.toInstant(), account);
    }

    @Path("id")
    @GET
    public Transaction findById(@QueryParam("date") Date date, @QueryParam("offset") Integer offset, @QueryParam("account") AccountEnum account) {
        Transaction transaction = queryService.findTransactionById(date, offset, account);
        if (transaction == null) {
            throw new NotFoundException("transaction not found");
        }
        return transaction;
    }

}
