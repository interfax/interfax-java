package net.interfax.rest.client.domain;

import java.util.Optional;

public class GetInboundFaxListOptions {

    private Optional<Boolean> unreadOnly = Optional.empty();
    private Optional<Integer> limit = Optional.empty();
    private Optional<Long> lastId = Optional.empty();
    private Optional<Boolean> allUsers = Optional.empty();

    public Optional<Boolean> getUnreadOnly() {
        return unreadOnly;
    }

    public void setUnreadOnly(final Optional<Boolean> unreadOnly) {
        this.unreadOnly = unreadOnly;
    }

    public Optional<Integer> getLimit() {
        return limit;
    }

    public void setLimit(final Optional<Integer> limit) {
        this.limit = limit;
    }

    public Optional<Long> getLastId() {
        return lastId;
    }

    public void setLastId(final Optional<Long> lastId) {
        this.lastId = lastId;
    }

    public Optional<Boolean> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(final Optional<Boolean> allUsers) {
        this.allUsers = allUsers;
    }
}
