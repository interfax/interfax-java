package net.interfax.rest.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutboundFaxStructure {

    private String id;
    private String uri;
    private int status;
    private String userId;
    private int pagesSent;
    private String completionTime;
    private String remoteCSID;
    private String duration;
    private String priority;
    private String units;
    private String costPerUnit;
    private String attemptsMade;
    private String pageSize;
    private String pageOrientation;
    private String pageResolution;
    private String rendering;
    private String pageHeader;
    private String submitTime;
    private String subject;
    private String destinationFax;
    private String replyEmail;
    private int pagesSubmitted;
    private int attemptsToPerform;
    private int contact;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public int getPagesSent() {
        return pagesSent;
    }

    public void setPagesSent(final int pagesSent) {
        this.pagesSent = pagesSent;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(final String completionTime) {
        this.completionTime = completionTime;
    }

    public String getRemoteCSID() {
        return remoteCSID;
    }

    public void setRemoteCSID(final String remoteCSID) {
        this.remoteCSID = remoteCSID;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(final String duration) {
        this.duration = duration;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(final String units) {
        this.units = units;
    }

    public String getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(final String costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public String getAttemptsMade() {
        return attemptsMade;
    }

    public void setAttemptsMade(final String attemptsMade) {
        this.attemptsMade = attemptsMade;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(final String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageOrientation() {
        return pageOrientation;
    }

    public void setPageOrientation(final String pageOrientation) {
        this.pageOrientation = pageOrientation;
    }

    public String getPageResolution() {
        return pageResolution;
    }

    public void setPageResolution(final String pageResolution) {
        this.pageResolution = pageResolution;
    }

    public String getRendering() {
        return rendering;
    }

    public void setRendering(final String rendering) {
        this.rendering = rendering;
    }

    public String getPageHeader() {
        return pageHeader;
    }

    public void setPageHeader(final String pageHeader) {
        this.pageHeader = pageHeader;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(final String submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getDestinationFax() {
        return destinationFax;
    }

    public void setDestinationFax(final String destinationFax) {
        this.destinationFax = destinationFax;
    }

    public String getReplyEmail() {
        return replyEmail;
    }

    public void setReplyEmail(final String replyEmail) {
        this.replyEmail = replyEmail;
    }

    public int getPagesSubmitted() {
        return pagesSubmitted;
    }

    public void setPagesSubmitted(final int pagesSubmitted) {
        this.pagesSubmitted = pagesSubmitted;
    }

    public int getAttemptsToPerform() {
        return attemptsToPerform;
    }

    public void setAttemptsToPerform(final int attemptsToPerform) {
        this.attemptsToPerform = attemptsToPerform;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(final int contact) {
        this.contact = contact;
    }
}
