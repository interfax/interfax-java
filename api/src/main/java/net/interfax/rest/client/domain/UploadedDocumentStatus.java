package net.interfax.rest.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadedDocumentStatus {

    private String userId;
    private String fileName;
    private long fileSize;
    private long uploaded;
    private String uri;
    private Date creationTime;
    private Date lastusageTime;
    private String status;
    private String disposition;
    private String sharing;

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(final long fileSize) {
        this.fileSize = fileSize;
    }

    public long getUploaded() {
        return uploaded;
    }

    public void setUploaded(final long uploaded) {
        this.uploaded = uploaded;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastusageTime() {
        return lastusageTime;
    }

    public void setLastusageTime(final Date lastusageTime) {
        this.lastusageTime = lastusageTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(final String disposition) {
        this.disposition = disposition;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(final String sharing) {
        this.sharing = sharing;
    }
}
