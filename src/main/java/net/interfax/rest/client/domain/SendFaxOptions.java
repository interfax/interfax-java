package net.interfax.rest.client.domain;

import java.util.Date;
import java.util.Optional;

public class SendFaxOptions {

    private Optional<String> contact = Optional.empty();
    private Optional<Date> postponeTime = Optional.empty();
    private Optional<Integer> retriesToPerform = Optional.empty();
    private Optional<String> csid = Optional.empty();
    private Optional<String> pageHeader = Optional.empty();
    private Optional<String> reference = Optional.empty();
    private Optional<String> replyAddress = Optional.empty();
    private Optional<String> pageSize = Optional.empty();
    private Optional<String> fitToPage = Optional.empty();
    private Optional<String> pageOrientation = Optional.empty();
    private Optional<String> resolution = Optional.empty();
    private Optional<String> rendering = Optional.empty();

    public Optional<String> getContact() {
        return contact;
    }

    public void setContact(final Optional<String> contact) {
        this.contact = contact;
    }

    public Optional<Date> getPostponeTime() {
        return postponeTime;
    }

    public void setPostponeTime(final Optional<Date> postponeTime) {
        this.postponeTime = postponeTime;
    }

    public Optional<Integer> getRetriesToPerform() {
        return retriesToPerform;
    }

    public void setRetriesToPerform(final Optional<Integer> retriesToPerform) {
        this.retriesToPerform = retriesToPerform;
    }

    public Optional<String> getCsid() {
        return csid;
    }

    public void setCsid(final Optional<String> csid) {
        this.csid = csid;
    }

    public Optional<String> getPageHeader() {
        return pageHeader;
    }

    public void setPageHeader(final Optional<String> pageHeader) {
        this.pageHeader = pageHeader;
    }

    public Optional<String> getReference() {
        return reference;
    }

    public void setReference(final Optional<String> reference) {
        this.reference = reference;
    }

    public Optional<String> getReplyAddress() {
        return replyAddress;
    }

    public void setReplyAddress(final Optional<String> replyAddress) {
        this.replyAddress = replyAddress;
    }

    public Optional<String> getPageSize() {
        return pageSize;
    }

    public void setPageSize(final Optional<String> pageSize) {
        this.pageSize = pageSize;
    }

    public Optional<String> getFitToPage() {
        return fitToPage;
    }

    public void setFitToPage(final Optional<String> fitToPage) {
        this.fitToPage = fitToPage;
    }

    public Optional<String> getPageOrientation() {
        return pageOrientation;
    }

    public void setPageOrientation(final Optional<String> pageOrientation) {
        this.pageOrientation = pageOrientation;
    }

    public Optional<String> getResolution() {
        return resolution;
    }

    public void setResolution(final Optional<String> resolution) {
        this.resolution = resolution;
    }

    public Optional<String> getRendering() {
        return rendering;
    }

    public void setRendering(final Optional<String> rendering) {
        this.rendering = rendering;
    }
}
