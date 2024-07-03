package com.transactionHub.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;

@MongoEntity(collection="tags")
public class Tag {
    @BsonId
    public String name;
    public String description;
}
