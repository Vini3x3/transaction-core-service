package com.transactionHub.service;

import com.transactionHub.repository.TagRepository;
import com.transactionHub.transactionCoreLibrary.domain.Tag;
import com.transactionHub.util.helper.TagTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TagService {

    @Inject
    TagRepository tagRepository;

    public void createOrUpdate(Tag tag) {
        tagRepository.persistOrUpdate(TagTranslator.mapToEntity(tag));
    }

    public Tag find(String tag) {
        var entity = tagRepository.find("_id", tag).firstResult();
        if (entity == null) {
            return null;
        } else {
            return TagTranslator.mapToDomain(entity);
        }
    }

    public void delete(String tag) {
        tagRepository.delete("_id", tag);
    }

}
