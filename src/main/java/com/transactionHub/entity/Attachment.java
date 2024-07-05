package com.transactionHub.entity;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public class Attachment {
    private byte[] content;
    private String filename;
    private Date uploadDate;
    private Map<String, Object> meta;

    public Attachment() {
    }

    public Attachment(String filename, Date uploadDate, Map<String, Object> meta, byte[] content) {
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

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
    
}
