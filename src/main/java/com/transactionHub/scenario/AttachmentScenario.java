package com.transactionHub.scenario;


import com.transactionHub.entity.Attachment;
import com.transactionHub.service.AttachmentService;
import com.transactionHub.service.ModifyService;
import com.transactionHub.service.QueryService;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.transactionCoreLibrary.domain.Transaction;
import com.transactionHub.util.helper.WebHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class AttachmentScenario {

    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_OFFSET = "transaction_offset";
    public static final String TRANSACTION_ACCOUNT = "transaction_account";

    @Inject
    ModifyService modifyService;

    @Inject
    QueryService queryService;

    @Inject
    AttachmentService attachmentService;

    public String saveAttachment(Instant date, int offset, AccountEnum account, InputStream inputStream, String filename) {
        Transaction attachedTransaction = queryService.findTransactionById(date, offset, account);
        if (attachedTransaction == null) {
            throw new NotFoundException("transaction not found!");
        }

        var fileMeta = new HashMap<String, Object>(Map.of(
                TRANSACTION_DATE, attachedTransaction.getDate(),
                TRANSACTION_OFFSET, attachedTransaction.getOffset(),
                TRANSACTION_ACCOUNT, attachedTransaction.getAccount()
        ));
        String attachmentId = attachmentService.save(fileMeta, inputStream, filename);

        modifyService.addAttachment(date, offset, account, filename, attachmentId);

        return attachmentId;
    }

    public Attachment downloadAttachment(String attachmentId) {
        return attachmentService.get(attachmentId);
    }

    public void deleteAttachment(String attachmentId) {
        Attachment attachment = attachmentService.get(attachmentId);
        if (attachment == null) {
            throw new NotFoundException("attachment not found");
        }
        attachmentService.delete(attachmentId);

        Instant date = (Instant)attachment.getMeta().get(TRANSACTION_DATE);
        Integer offset = (Integer)attachment.getMeta().get(TRANSACTION_OFFSET);
        AccountEnum accountEnum = WebHelper.parseAccountEnum((String)attachment.getMeta().get(TRANSACTION_ACCOUNT));

        modifyService.detachAttachment(date, offset, accountEnum, attachmentId);
    }

}
