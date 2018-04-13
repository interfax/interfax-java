package net.interfax.rest.client.domain;

import net.interfax.rest.client.domain.enums.Disposition;
import net.interfax.rest.client.domain.enums.Sharing;

import java.util.Optional;

public class DocumentUploadSessionOptions {

    private Optional<Long> size;
    private Optional<String> name;
    private Optional<Disposition> disposition;
    private Optional<Sharing> sharing;

    public Optional<Long> getSize() {
        return size == null ? Optional.empty() : size;
    }

    public void setSize(final Optional<Long> size) {
        this.size = size;
    }

    public Optional<String> getName() {
        return name == null ? Optional.empty() : name;
    }

    public void setName(final Optional<String> name) {
        this.name = name;
    }

    public Optional<Disposition> getDisposition() {
        return disposition == null ? Optional.empty() : disposition;
    }

    public void setDisposition(final Optional<Disposition> disposition) {
        this.disposition = disposition;
    }

    public Optional<Sharing> getSharing() {
        return sharing == null ? Optional.empty() : sharing;
    }

    public void setSharing(final Optional<Sharing> sharing) {
        this.sharing = sharing;
    }
}
