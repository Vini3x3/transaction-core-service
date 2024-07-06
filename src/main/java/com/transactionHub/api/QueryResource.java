package com.transactionHub.api;

import com.transactionHub.service.QueryService;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.transactionHub.util.helper.WebHelper.convertToInstant;

@Path("/query")
public class QueryResource {

    @Inject
    QueryService queryService;

    @Path("date-range")
    @GET
    public List<Transaction> findByDateRange(@QueryParam("start") Date start, @QueryParam("end") Date end, @QueryParam("account") AccountEnum account) {
        return queryService.findTransactionsByDate(convertToInstant(start), convertToInstant(end), account);
    }

    @Path("id")
    @GET
    public Transaction findById(@QueryParam("date") Date date, @QueryParam("offset") Integer offset, @QueryParam("account") AccountEnum account) {
        Transaction transaction = queryService.findTransactionById(convertToInstant(date), offset, account);
        if (transaction == null) {
            throw new NotFoundException("transaction not found");
        }
        return transaction;
    }

}
