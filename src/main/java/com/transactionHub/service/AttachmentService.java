package com.transactionHub.service;

import com.transactionHub.entity.Attachment;
import com.transactionHub.repository.AttachmentRepository;
import com.transactionHub.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.util.Map;

@ApplicationScoped
public class AttachmentService {

    @Inject
    AttachmentRepository attachmentRepository;

    public Attachment get(String attachmentId) {
        return attachmentRepository.download(attachmentId);
    }

    // return attachmentId after save
    public String save(Map<String, Object> meta, InputStream inputStream, String filename) {
        return attachmentRepository.save(meta, inputStream, filename);
    }

    public void delete(String attachmentId) {
        attachmentRepository.delete(attachmentId);
    }

}
