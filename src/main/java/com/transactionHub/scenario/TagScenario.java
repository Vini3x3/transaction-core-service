package com.transactionHub.scenario;

import com.transactionHub.service.TagService;
import com.transactionHub.transactionCoreLibrary.domain.Tag;
import com.transactionHub.transactionCoreLibrary.util.TagPolicy;
import com.transactionHub.util.helper.TagTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TagScenario {

    @Inject
    TagService tagService;

    public void createOrUpdate(Tag tag) {
        verifyTag(tag);
        tagService.createOrUpdate(tag);
    }

    public Tag find(String tag) {
        return tagService.read(tag);
    }

    public void delete(String tag) {
        verifyTag(tag);
        tagService.delete(tag);
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
