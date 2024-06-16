package com.transactionHub.entity;

public class TransactionTranslator {

    public static Transaction mapToEntity(com.transactionHub.transactionCoreLibrary.domain.Transaction transaction) {
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

        return entity;
    }

    public static com.transactionHub.transactionCoreLibrary.domain.Transaction mapToDomain(Transaction transaction) {
        var entity = new com.transactionHub.transactionCoreLibrary.domain.Transaction();

        entity.setDate(transaction.id.date);
        entity.setOffset(transaction.id.offset);
        entity.setAccount(transaction.id.account);

        entity.setDescription(transaction.description);
        entity.setDeposit(transaction.deposit);
        entity.setWithdrawal(transaction.withdrawal);
        entity.setBalance(transaction.balance);

        entity.setTags(transaction.tags);
        entity.setMeta(transaction.metas);

        return entity;
    }
}
