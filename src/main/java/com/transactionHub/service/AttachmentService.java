package com.transactionHub.service;

import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

@ApplicationScoped
public class AttachmentService {

    public byte[] get(String attachmentId) {
        return null;
    }

    public Map<String, String> getMeta(String attachmentId) {
        return null;
    }

    // return attachmentId after save
    public String save(Date date, int offset, AccountEnum account, InputStream inputStream, String filename) {
        return "";
    }

    public void delete(Date date, int offset, AccountEnum account, String attachmentId) {

    }

}
