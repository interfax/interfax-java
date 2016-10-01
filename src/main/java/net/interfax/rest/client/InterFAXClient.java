package net.interfax.rest.client;

import net.interfax.rest.client.domain.APIResponse;

import java.io.File;

public interface InterFAXClient {

    // Sending faxes

    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax);

    public APIResponse sendFax(final String faxNumber, final File[] fileToSendAsFax);

    // Uploading documents

    public APIResponse uploadDocument(final File fileToUpload);

}
