package com.transactionHub.entity;

import java.time.ZonedDateTime;
import java.util.Map;

public class Attachment {
    private byte[] content;
    private String filename;
    private ZonedDateTime uploadDate;
    private Map<String, Object> meta;

    public Attachment() {
    }

    public Attachment(String filename, ZonedDateTime uploadDate, Map<String, Object> meta, byte[] content) {
        this.content = content;
        this.filename = filename;
        this.uploadDate = uploadDate;
        this.meta = meta;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ZonedDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

}
