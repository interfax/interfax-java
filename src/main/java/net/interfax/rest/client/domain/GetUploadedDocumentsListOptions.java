package net.interfax.rest.client.domain;

import java.util.Optional;

public class GetUploadedDocumentsListOptions {

    private Optional<Integer> limit;
    private Optional<Integer> offset;

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
