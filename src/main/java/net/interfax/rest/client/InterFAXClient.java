package net.interfax.rest.client;

import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetInboundFaxListOptions;
import net.interfax.rest.client.domain.InboundFaxStructure;
import net.interfax.rest.client.domain.SearchFaxOptions;
import net.interfax.rest.client.domain.GetFaxListOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.OutboundFaxStructure;
import net.interfax.rest.client.domain.SendFaxOptions;
import net.interfax.rest.client.domain.UploadedDocumentStatus;
import net.interfax.rest.client.exception.UnsuccessfulStatusCodeException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface InterFAXClient {

    // Sending faxes

    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax) throws IOException;

    public APIResponse sendFax(final String faxNumber,
                               final File fileToSendAsFax,
                               final Optional<SendFaxOptions> options) throws IOException;

    public APIResponse sendFax(final String faxNumber, final File[] filesToSendAsFax) throws IOException;

    public APIResponse sendFax(final String faxNumber,
                               final File[] filesToSendAsFax,
                               final Optional<SendFaxOptions> options) throws IOException;

    public APIResponse sendFax(final String faxNumber, final String urlOfDoc);

    public APIResponse sendFax(final String faxNumber, final String urlOfDoc, final Optional<SendFaxOptions> options);

    public APIResponse resendFax(final String id, final Optional<String> faxNumber);

    public APIResponse hideFax(final String id);

    // Outbound fax operations

    public OutboundFaxStructure[] getFaxList() throws UnsuccessfulStatusCodeException;

    public OutboundFaxStructure[] getFaxList(final Optional<GetFaxListOptions> options)
            throws UnsuccessfulStatusCodeException;

    public OutboundFaxStructure[] getCompletedFaxList(final String[] ids) throws UnsuccessfulStatusCodeException;

    public OutboundFaxStructure getFaxRecord(final String id) throws UnsuccessfulStatusCodeException;

    public byte[] getOuboundFaxImage(final String id) throws UnsuccessfulStatusCodeException;

    public APIResponse cancelFax(final String id);

    public OutboundFaxStructure[] searchFaxList() throws UnsuccessfulStatusCodeException;

    public OutboundFaxStructure[] searchFaxList(Optional<SearchFaxOptions> options)
            throws UnsuccessfulStatusCodeException;

    // Documents

    public APIResponse uploadDocument(final File fileToUpload);

    public APIResponse uploadDocument(final File fileToUpload, Optional<DocumentUploadSessionOptions> options);

    public APIResponse uploadChunk(String uploadChunkToDocumentEndpoint,
                                   byte[] bytesToUpload,
                                   int startByteRange,
                                   int endByteRange,
                                   boolean lastChunk);

    public UploadedDocumentStatus[] getUploadedDocumentsList() throws UnsuccessfulStatusCodeException;

    public UploadedDocumentStatus[] getUploadedDocumentsList(Optional<GetUploadedDocumentsListOptions> options)
            throws UnsuccessfulStatusCodeException;

    public UploadedDocumentStatus getUploadedDocumentStatus(String documentId) throws UnsuccessfulStatusCodeException;

    public APIResponse cancelDocumentUploadSession(String documentId);

    // Accounts

    public Double getAccountCredits() throws UnsuccessfulStatusCodeException;

    // Receiving faxes

    public InboundFaxStructure[] getInboundFaxList() throws UnsuccessfulStatusCodeException;

    public InboundFaxStructure[] getInboundFaxList(final Optional<GetInboundFaxListOptions> options)
            throws UnsuccessfulStatusCodeException;

    public InboundFaxStructure getInboundFaxRecord(final String id) throws UnsuccessfulStatusCodeException;

    public byte[] getInboundFaxImage(final String id) throws UnsuccessfulStatusCodeException;

    // client lifecycle

    public void closeClient();

}
