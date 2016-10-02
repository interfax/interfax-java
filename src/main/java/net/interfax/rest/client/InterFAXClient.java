package net.interfax.rest.client;

import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.UploadedDocumentStatus;

import java.io.File;
import java.util.Optional;

public interface InterFAXClient {

    // Sending faxes

    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax);

    public APIResponse sendFax(final String faxNumber, final File[] fileToSendAsFax);

    // documents

    public APIResponse uploadDocument(final File fileToUpload);

    public APIResponse uploadDocument(final File fileToUpload, Optional<DocumentUploadSessionOptions> options);

    public APIResponse uploadChunk(String uploadChunkToDocumentEndpoint,
                                   byte[] bytesToUpload,
                                   int startByteRange,
                                   int endByteRange,
                                   boolean lastChunk);

    public UploadedDocumentStatus[] getUploadedDocumentsList();

    public UploadedDocumentStatus[] getUploadedDocumentsList(Optional<GetUploadedDocumentsListOptions> options);

    public UploadedDocumentStatus getUploadedDocumentStatus(String documentId);

    public APIResponse cancelDocumentUploadSession(String documentId);

    // client lifecycle

    public void closeClient();

}
