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

        entity.attachments = transaction.getAttachments()
                .stream()
                .map(TransactionTranslator::mapToFileInfoEntity)
                .toList();

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
                fileInfoList.add(mapToFileInfoDomain(item));
            }
        }

        entity.setAttachments(fileInfoList);
        entity.setMeta(transaction.metas);

        return entity;
    }

    public static FileInfo mapToFileInfoDomain(Map<String, Object> entity) {
        var fileInfo = new FileInfo();
        fileInfo.setFilename((String) entity.get("filename"));
        fileInfo.setFileId((String) entity.get("attachmentId"));
        fileInfo.setUpdateDate((Date) entity.get("updateDate"));
        return fileInfo;
    }

    public static Map<String, Object> mapToFileInfoEntity(FileInfo fileInfo) {
        return new HashMap<>(Map.of(
                "filename", fileInfo.getFilename(),
                "updateDate", fileInfo.getUpdateDate(),
                "attachmentId", fileInfo.getFileId()
        ));
    }

}
