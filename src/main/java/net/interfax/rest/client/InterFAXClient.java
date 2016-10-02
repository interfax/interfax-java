package net.interfax.rest.client;

import net.interfax.rest.client.domain.APIResponse;

import java.io.File;

public interface InterFAXClient {

    // Sending faxes

    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax);

    public APIResponse sendFax(final String faxNumber, final File[] fileToSendAsFax);

    // Uploading documents

    public APIResponse uploadDocument(final File fileToUpload);

    public APIResponse uploadChunk(
            String uploadChunkToDocumentEndpoint,
            byte[] bytesToUpload,
            int startByteRange,
            int endByteRange,
            boolean lastChunk);

    // client lifecycle

    public void closeClient();

}
