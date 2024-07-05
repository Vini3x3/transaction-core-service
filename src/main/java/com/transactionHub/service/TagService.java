package com.transactionHub.service;

import com.transactionHub.repository.TagRepository;
import com.transactionHub.transactionCoreLibrary.domain.Tag;
import com.transactionHub.transactionCoreLibrary.util.TagPolicy;
import com.transactionHub.util.helper.TagTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    public void createOrUpdate(Tag tag) {
        verifyTag(tag);
        tagRepository.persistOrUpdate(TagTranslator.mapToEntity(tag));
    }

    public Tag read(String tag) {
        var entity = tagRepository.find("_id", tag).firstResult();
        if (entity == null) {
            return null;
        } else {
            return TagTranslator.mapToDomain(entity);
        }
    }

    public void delete(String tag) {
        verifyTag(tag);
        tagRepository.delete("_id", tag);
    }


    private void verifyTag(Tag tag) {
        if (TagPolicy.isSystemTag(tag)) {
            throw new IllegalArgumentException("System tag is not allowed!");
        }
        if (tag.getDescription() == null || tag.getDescription().isBlank()) {
            throw new IllegalArgumentException("Tag has empty description!");
        }
    }

    private void verifyTag(String tag) {
        if (TagPolicy.isSystemTag(tag)) {
            throw new IllegalArgumentException("System tag is not allowed!");
        }
    }

}
