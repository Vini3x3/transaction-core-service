package com.transactionHub.service;

import com.transactionHub.repository.TransactionRepository;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

@ApplicationScoped
public class ModifyService {

    @Inject
    TransactionRepository transactionRepository;

    public void addAttachment(Instant date, int offset, AccountEnum accountEnum, String filename, String attachmentId) {
        var transaction = transactionRepository.findById(date, offset, accountEnum);
        var file = new HashMap<String, Object>();
        file.put("filename", filename);
        file.put("attachmentId", attachmentId);
        file.put("updateDate", Instant.now());
        if (transaction.attachments == null) {
            transaction.attachments = new ArrayList<>();
        }
        transaction.attachments.add(file);
        transactionRepository.update(transaction);
    }

    public void detachAttachment(Instant date, int offset, AccountEnum accountEnum, String attachmentId) {
        var transaction = transactionRepository.findById(date, offset, accountEnum);
        if (transaction.attachments == null) {
            transaction.attachments = new ArrayList<>();
        }
        transaction.attachments = transaction.attachments
                .stream()
                .filter(o -> !o.get("attachmentId").equals(attachmentId))
                .toList();
        transactionRepository.update(transaction);
    }

}
