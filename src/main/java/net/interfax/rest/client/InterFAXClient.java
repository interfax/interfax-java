package net.interfax.rest.client;

import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.OutboundFaxStructure;
import net.interfax.rest.client.domain.SendFaxOptions;
import net.interfax.rest.client.domain.UploadedDocumentStatus;

import java.io.File;
import java.util.Optional;

public interface InterFAXClient {

    // Sending faxes

    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax);

    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax, final Optional<SendFaxOptions> options);

    public APIResponse sendFax(final String faxNumber, final File[] filesToSendAsFax);

    public APIResponse sendFax(final String faxNumber, final File[] filesToSendAsFax, final Optional<SendFaxOptions> options);

    public APIResponse sendFax(final String faxNumber, final String urlOfDoc);

    public APIResponse sendFax(final String faxNumber, final String urlOfDoc, final Optional<SendFaxOptions> options);

    public OutboundFaxStructure[] getFaxList();

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
