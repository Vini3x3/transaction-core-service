package com.transactionHub.util.helper;

import com.transactionHub.entity.Transaction;
import com.transactionHub.transactionCoreLibrary.domain.FileInfo;

import java.util.*;

public class TransactionTranslator {

    public static Transaction mapToEntity(com.transactionHub.transactionCoreLibrary.domain.Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        var entity = new Transaction();

        entity.id = new Transaction.CompositeId();
        entity.id.date = transaction.getDate();
        entity.id.account = transaction.getAccount();
        entity.id.offset = transaction.getOffset();

        entity.description = transaction.getDescription();
        entity.withdrawal = transaction.getWithdrawal();
        entity.deposit = transaction.getDeposit();
        entity.balance = transaction.getBalance();
        entity.tags = transaction.getTags();
        entity.metas = transaction.getMeta();

        List<Map<String, Object>> convertedMaps = new ArrayList<>();
        for (var attachment: transaction.getAttachments()) {
            convertedMaps.add(new HashMap<>(Map.of(
                    "filename", attachment.getFilename(),
                    "updateDate", attachment.getUpdateDate(),
                    "attachmentId", attachment.getFileId()
            )));
        }
        entity.attachments = convertedMaps;

        return entity;
    }

    public static com.transactionHub.transactionCoreLibrary.domain.Transaction mapToDomain(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        var entity = new com.transactionHub.transactionCoreLibrary.domain.Transaction();

        entity.setDate(transaction.id.date);
        entity.setOffset(transaction.id.offset);
        entity.setAccount(transaction.id.account);

        entity.setDescription(transaction.description);
        entity.setDeposit(transaction.deposit);
        entity.setWithdrawal(transaction.withdrawal);
        entity.setBalance(transaction.balance);

        entity.setTags(transaction.tags);

        List<FileInfo> fileInfoList = new ArrayList<>();
        if (transaction.attachments != null && !transaction.attachments.isEmpty()) {
            for (var item : transaction.attachments) {
                var fileInfo = new FileInfo();
                fileInfo.setFilename((String) item.get("filename"));
                fileInfo.setFileId((String) item.get("attachmentId"));
                fileInfo.setUpdateDate((Date) item.get("updateDate"));
                fileInfoList.add(fileInfo);
            }
        }

        entity.setAttachments(fileInfoList);
        entity.setMeta(transaction.metas);

        return entity;
    }

}
