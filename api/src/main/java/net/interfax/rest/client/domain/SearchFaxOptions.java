package net.interfax.rest.client.domain;

import java.util.Date;
import java.util.Optional;

public class SearchFaxOptions {

    private Optional<String> ids = Optional.empty();
    private Optional<String> reference = Optional.empty();
    private Optional<Date> dateFrom = Optional.empty();
    private Optional<Date> dateTo = Optional.empty();
    private Optional<String> status = Optional.empty();
    private Optional<String> userId = Optional.empty();
    private Optional<String> faxNumber = Optional.empty();
    private Optional<Integer> limit = Optional.empty();
    private Optional<Integer> offset = Optional.empty();

    public Optional<String> getIds() {
        return ids;
    }

    public void setIds(final Optional<String> ids) {
        this.ids = ids;
    }

    public Optional<String> getReference() {
        return reference;
    }

    public void setReference(final Optional<String> reference) {
        this.reference = reference;
    }

    public Optional<Date> getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(final Optional<Date> dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Optional<Date> getDateTo() {
        return dateTo;
    }

    public void setDateTo(final Optional<Date> dateTo) {
        this.dateTo = dateTo;
    }

    public Optional<String> getStatus() {
        return status;
    }

    public void setStatus(final Optional<String> status) {
        this.status = status;
    }

    public Optional<String> getUserId() {
        return userId;
    }

    public void setUserId(final Optional<String> userId) {
        this.userId = userId;
    }

    public Optional<String> getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(final Optional<String> faxNumber) {
        this.faxNumber = faxNumber;
    }

    public Optional<Integer> getLimit() {
        return limit;
    }

    public void setLimit(final Optional<Integer> limit) {
        this.limit = limit;
    }

    public Optional<Integer> getOffset() {
        return offset;
    }

    public void setOffset(final Optional<Integer> offset) {
        this.offset = offset;
    }
}
