package net.interfax.rest.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InboundFaxStructure {

    private String userId;
    private String phoneNumber;
    private String remoteCSID;
    private String callerId;
    private String imageStatus;
    private Date receiveTime;
    private long messageId;
    private long messageStatus;
    private long pages;
    private long messageSize;
    private long messageType;
    private long recordingDuration;
    private long numOfEmails;
    private long numOfFailedEmails;

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRemoteCSID() {
        return remoteCSID;
    }

    public void setRemoteCSID(final String remoteCSID) {
        this.remoteCSID = remoteCSID;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(final String callerId) {
        this.callerId = callerId;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(final String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(final Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(final long messageId) {
        this.messageId = messageId;
    }

    public long getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(final long messageStatus) {
        this.messageStatus = messageStatus;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(final long pages) {
        this.pages = pages;
    }

    public long getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(final long messageSize) {
        this.messageSize = messageSize;
    }

    public long getMessageType() {
        return messageType;
    }

    public void setMessageType(final long messageType) {
        this.messageType = messageType;
    }

    public long getRecordingDuration() {
        return recordingDuration;
    }

    public void setRecordingDuration(final long recordingDuration) {
        this.recordingDuration = recordingDuration;
    }

    public long getNumOfEmails() {
        return numOfEmails;
    }

    public void setNumOfEmails(final long numOfEmails) {
        this.numOfEmails = numOfEmails;
    }

    public long getNumOfFailedEmails() {
        return numOfFailedEmails;
    }

    public void setNumOfFailedEmails(final long numOfFailedEmails) {
        this.numOfFailedEmails = numOfFailedEmails;
    }
}
