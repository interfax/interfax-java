package net.interfax.rest.client.domain;

import java.util.Optional;

public class GetFaxListOptions {

    private Optional<Integer> limit = Optional.empty();
    private Optional<String> lastId = Optional.empty();
    private Optional<String> sortOrder = Optional.empty();
    private Optional<String> userId = Optional.empty();

    public Optional<Integer> getLimit() {
        return limit;
    }

    public void setLimit(final Optional<Integer> limit) {
        this.limit = limit;
    }

    public Optional<String> getLastId() {
        return lastId;
    }

    public void setLastId(final Optional<String> lastId) {
        this.lastId = lastId;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(final Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Optional<String> getUserId() {
        return userId;
    }

    public void setUserId(final Optional<String> userId) {
        this.userId = userId;
    }
}
