package com.transactionHub.entity;

import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@MongoEntity(collection="transactions")
public class Transaction  {

    @BsonId
    public CompositeId id; // used by MongoDB for the _id field
    public String description;
    public BigDecimal withdrawal;
    public BigDecimal deposit;
    public BigDecimal balance;
    public Set<String> tags;
    public Map<String, String> metas;


    public static class CompositeId {
        public Date date;
        public int offset;
        public AccountEnum account;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompositeId that = (CompositeId) o;
            return date.compareTo(that.date) == 0 &&
                    offset == that.offset &&
                    account.equals(that.account);
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, offset, account);
        }
    }

}
