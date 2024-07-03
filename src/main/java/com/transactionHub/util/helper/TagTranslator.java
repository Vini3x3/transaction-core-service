package com.transactionHub.util.helper;

import com.transactionHub.entity.Tag;

public class TagTranslator {

    public static Tag mapToEntity(com.transactionHub.transactionCoreLibrary.domain.Tag tag) {
        var entity = new Tag();

        entity.name = tag.getName();
        entity.description = tag.getDescription();

        return entity;
    }

    public static com.transactionHub.transactionCoreLibrary.domain.Tag mapToDomain(Tag tag) {
        return new com.transactionHub.transactionCoreLibrary.domain.Tag(tag.name, tag.description);
    }

}
