package com.transactionHub.api;

import com.transactionHub.service.QueryService;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.jboss.resteasy.reactive.RestPath;

import java.util.Date;
import java.util.List;

@Path("/query")
public class QueryResource {

    @Inject
    protected QueryService queryService;

    @Path("date-range")
    @GET
    public List<Transaction> findByDateRange(@QueryParam("start") Date start, @QueryParam("end") Date end, @QueryParam("account") AccountEnum account) {
        return queryService.findTransactionsByDate(start, end, account);
    }

}
