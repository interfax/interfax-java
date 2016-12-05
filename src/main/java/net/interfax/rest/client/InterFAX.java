package net.interfax.rest.client;

import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetInboundFaxListOptions;
import net.interfax.rest.client.domain.InboundFaxStructure;
import net.interfax.rest.client.domain.InboundFaxesEmailsStructure;
import net.interfax.rest.client.domain.SearchFaxOptions;
import net.interfax.rest.client.domain.GetFaxListOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.OutboundFaxStructure;
import net.interfax.rest.client.domain.SendFaxOptions;
import net.interfax.rest.client.domain.UploadedDocumentStatus;
import net.interfax.rest.client.exception.UnsuccessfulStatusCodeException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface InterFAX {

    // Sending faxes

    /**
     * Send a single file as fax
     *
     * @param faxNumber       number to fax to
     * @param fileToSendAsFax file to send as fax
     * @return {@link APIResponse}
     * @throws IOException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2918">https://www.interfax.net/en/dev/rest/reference/2918</a>
     */
    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax) throws IOException;

    /**
     * Send a single file as fax with additional {@link SendFaxOptions}
     *
     * @param faxNumber       number to fax to
     * @param fileToSendAsFax file to send as fax
     * @param options         {@link SendFaxOptions} to use when sending the fax
     * @return {@link APIResponse}
     * @throws IOException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2918">https://www.interfax.net/en/dev/rest/reference/2918</a>
     */
    public APIResponse sendFax(final String faxNumber,
                               final File fileToSendAsFax,
                               final Optional<SendFaxOptions> options) throws IOException;
    
    /**
     * Send an array of files as a fax
     *
     * @param faxNumber        number to fax to
     * @param filesToSendAsFax array of files to send as fax
     * @return {@link APIResponse}
     * @throws IOException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2918">https://www.interfax.net/en/dev/rest/reference/2918</a>
     */
    public APIResponse sendFax(final String faxNumber, final File[] filesToSendAsFax) throws IOException;

    /**
     * Send an array of input streams as a fax
     *
     * @param faxNumber        number to fax to
     * @param streamsToSendAsFax array of input streams to send as fax
     * @param fileNames array of file names corresponding to the input streams (for mime detection)
     * @return {@link APIResponse}
     * @throws IOException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2918">https://www.interfax.net/en/dev/rest/reference/2918</a>
     */
    public APIResponse sendFax(final String faxNumber,
                               final InputStream[] streamsToSendAsFax,
                               final String fileNames[]) throws IOException;

    /**
     * Send an array of files as a fax with additional {@link SendFaxOptions}
     *
     * @param faxNumber        number to fax to
     * @param filesToSendAsFax array of files to send as fax
     * @param options          {@link SendFaxOptions} to use when sending the fax
     * @return {@link APIResponse}
     * @throws IOException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2918">https://www.interfax.net/en/dev/rest/reference/2918</a>
     */
    public APIResponse sendFax(final String faxNumber,
                               final File[] filesToSendAsFax,
                               final Optional<SendFaxOptions> options) throws IOException;

    /**
     * Send an array of input streams as a fax with additional {@link SendFaxOptions}
     *
     * @param faxNumber        number to fax to
     * @param streamsToSendAsFax array of files to send as fax
     * @param fileNames array of file names corresponding to the input streams (for mime detection)
     * @param options          {@link SendFaxOptions} to use when sending the fax
     * @return {@link APIResponse}
     * @throws IOException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2918">https://www.interfax.net/en/dev/rest/reference/2918</a>
     */
    public APIResponse sendFax(final String faxNumber,
                               final InputStream[] streamsToSendAsFax,
                               final String fileNames[],
                               final Optional<SendFaxOptions> options) throws IOException;

    /**
     * Send a pre-uploaded document, available on a HTTP url, as a fax
     *
     * @param faxNumber number to fax to
     * @param urlOfDoc  url of doc to send as fax
     * @return {@link APIResponse}
     */
    public APIResponse sendFax(final String faxNumber, final String urlOfDoc);

    /**
     * Send a pre-uploaded document, available on a HTTP url, as a fax with additional {@link SendFaxOptions}
     *
     * @param faxNumber number to fax to
     * @param urlOfDoc  url of doc to send as fax
     * @param options   {@link SendFaxOptions} to use when sending the fax
     * @return {@link APIResponse}
     */
    public APIResponse sendFax(final String faxNumber, final String urlOfDoc, final Optional<SendFaxOptions> options);

    /**
     * Resend a previously-submitted fax, without needing to re-upload the original document
     *
     * @param id the ID of the fax to be resent.
     * @param faxNumber optional faxNumber to resend to; if not provided defaults to the fax number to which this fax
     *                  was previously sent.
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2908">https://www.interfax.net/en/dev/rest/reference/2908</a>
     */
    public APIResponse resendFax(final String id, final Optional<String> faxNumber);

    // Outbound fax operations

    /**
     * Get a list of recent outbound faxes (which does not include batch faxes)
     *
     * @return array of {@link OutboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2920">https://www.interfax.net/en/dev/rest/reference/2920</a>
     */
    public OutboundFaxStructure[] getFaxList() throws UnsuccessfulStatusCodeException;

    /**
     * Get a list of recent outbound faxes (which does not include batch faxes) with {@link GetFaxListOptions}
     *
     * @param options {@link GetFaxListOptions} to make the request with
     * @return array of {@link OutboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2920">https://www.interfax.net/en/dev/rest/reference/2920</a>
     */
    public OutboundFaxStructure[] getFaxList(final Optional<GetFaxListOptions> options)
            throws UnsuccessfulStatusCodeException;

    /**
     * Get details for a subset of completed faxes from a submitted list. (Submitted id's which have not completed are
     * ignored).
     *
     * @param ids comma-delimited list of fax id's to retrieve, if they have completed.
     * @return array of {@link OutboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2972">https://www.interfax.net/en/dev/rest/reference/2972</a>
     */
    public OutboundFaxStructure[] getCompletedFaxList(final String[] ids) throws UnsuccessfulStatusCodeException;

    /**
     * Retrieves information regarding a previously-submitted fax, including its current status
     *
     * @param id the ID of the fax for which to retrieve data.
     * @return {@link OutboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2921">https://www.interfax.net/en/dev/rest/reference/2921</a>
     */
    public OutboundFaxStructure getFaxRecord(final String id) throws UnsuccessfulStatusCodeException;

    /**
     * Retrieve the fax image (TIFF file) of a submitted fax
     *
     * @param id the ID of the fax for which to retrieve the image.
     * @return byte[] representation of the image
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2941">https://www.interfax.net/en/dev/rest/reference/2941</a>
     */
    public byte[] getOuboundFaxImage(final String id) throws UnsuccessfulStatusCodeException;

    /**
     * Cancel a fax in progress
     *
     * @param id ID of the fax to be cancelled. Note: This operation may be applied to single faxes or to individual faxes in a batch.
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2939">https://www.interfax.net/en/dev/rest/reference/2939</a>
     */
    public APIResponse cancelFax(final String id);

    /**
     * Search for outbound faxes
     *
     * @return array of {@link OutboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2959">https://www.interfax.net/en/dev/rest/reference/2959</a>
     */
    public OutboundFaxStructure[] searchFaxList() throws UnsuccessfulStatusCodeException;

    /**
     * Search for outbound faxes with {@link SearchFaxOptions}
     *
     * @param options {@link SearchFaxOptions} to make the request with
     * @return array of {@link OutboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2959">https://www.interfax.net/en/dev/rest/reference/2959</a>
     */
    public OutboundFaxStructure[] searchFaxList(Optional<SearchFaxOptions> options)
            throws UnsuccessfulStatusCodeException;

    /**
     * Hide a fax from listing in queries (there is no way to unhide a fax)
     *
     * @param id ID of fax to be hidden
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2940">https://www.interfax.net/en/dev/rest/reference/2940</a>
     */
    public APIResponse hideFax(final String id);

    // Documents

    /**
     * Upload contents of file as a document to send later as a fax. Suitable for large files as the upload process
     * automatically breaks the document into 1 MB chunks and uploads them
     *
     * @param fileToUpload file to upload
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2963">https://www.interfax.net/en/dev/rest/reference/2963</a>
     */
    public APIResponse uploadDocument(final File fileToUpload);

    /**
     * Upload contents of file as a document to send later as a fax, with {@link DocumentUploadSessionOptions}. Suitable
     * for large files as the upload process automatically breaks the document into 1 MB chunks and uploads them
     *
     * @param fileToUpload file to upload
     * @param options {@link DocumentUploadSessionOptions}
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2963">https://www.interfax.net/en/dev/rest/reference/2963</a>
     */
    public APIResponse uploadDocument(final File fileToUpload, Optional<DocumentUploadSessionOptions> options);

    /**
     * Upload chunks to an existing document upload session
     *
     * @param uploadChunkToDocumentEndpoint
     * @param bytesToUpload
     * @param startByteRange
     * @param endByteRange
     * @param lastChunk
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2966">https://www.interfax.net/en/dev/rest/reference/2966</a>
     */
    public APIResponse uploadChunk(String uploadChunkToDocumentEndpoint,
                                   byte[] bytesToUpload,
                                   int startByteRange,
                                   int endByteRange,
                                   boolean lastChunk);

    /**
     * Get a list of previous document uploads which are currently available
     *
     * @return array of {@link UploadedDocumentStatus}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2968">https://www.interfax.net/en/dev/rest/reference/2968</a>
     */
    public UploadedDocumentStatus[] getUploadedDocumentsList() throws UnsuccessfulStatusCodeException;

    /**
     * Get a list of previous document uploads which are currently available with {@link GetUploadedDocumentsListOptions}
     *
     * @param options {@link GetUploadedDocumentsListOptions} to make the request with
     * @return array of {@link UploadedDocumentStatus}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2968">https://www.interfax.net/en/dev/rest/reference/2968</a>
     */
    public UploadedDocumentStatus[] getUploadedDocumentsList(Optional<GetUploadedDocumentsListOptions> options)
            throws UnsuccessfulStatusCodeException;

    /**
     * Get the current status of a specific document upload
     *
     * @param documentId the ID of the upload to be queried
     * @return {@link UploadedDocumentStatus}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2965">https://www.interfax.net/en/dev/rest/reference/2965</a>
     */
    public UploadedDocumentStatus getUploadedDocumentStatus(String documentId) throws UnsuccessfulStatusCodeException;

    /**
     * Cancel a document upload and tear down the upload session, or delete a previous upload
     *
     * @param documentId the ID of the document upload session to be closed
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2964">https://www.interfax.net/en/dev/rest/reference/2964</a>
     */
    public APIResponse cancelDocumentUploadSession(String documentId);

    // Accounts

    /**
     * Determine the remaining faxing credits in your account
     *
     * @return Value of outstanding outbound credits in account, in the account's currency
     * @throws {@link UnsuccessfulStatusCodeException} exception with status code and response body if present
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/3001">https://www.interfax.net/en/dev/rest/reference/3001</a>
     */
    public Double getAccountCredits() throws UnsuccessfulStatusCodeException;

    // Receiving faxes

    /**
     * Retrieves a user's list of inbound faxes. (Sort order is always in descending ID)
     *
     * @return array of {@link InboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2935">https://www.interfax.net/en/dev/rest/reference/2935</a>
     */
    public InboundFaxStructure[] getInboundFaxList() throws UnsuccessfulStatusCodeException;

    /**
     * Retrieves a user's list of inbound faxes. (Sort order is always in descending ID) with {@link GetInboundFaxListOptions}
     *
     * @param options {@link GetInboundFaxListOptions} to make the request with
     * @return array of {@link InboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2935">https://www.interfax.net/en/dev/rest/reference/2935</a>
     */
    public InboundFaxStructure[] getInboundFaxList(final Optional<GetInboundFaxListOptions> options)
            throws UnsuccessfulStatusCodeException;

    /**
     * Retrieves a single fax's metadata (receive time, sender number, etc.)
     *
     * @param id the ID of the fax data to be retrieved
     * @return {@link InboundFaxStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2938">https://www.interfax.net/en/dev/rest/reference/2938</a>
     */
    public InboundFaxStructure getInboundFaxRecord(final String id) throws UnsuccessfulStatusCodeException;

    /**
     * Retrieves a single fax's image
     *
     * @param id the ID of the fax image to be retrieved
     * @return byte array representation of the fax image
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2937">https://www.interfax.net/en/dev/rest/reference/2937</a>
     */
    public byte[] getInboundFaxImage(final String id) throws UnsuccessfulStatusCodeException;

    /**
     * Retrieve the list of email addresses to which a fax was forwarded
     *
     * @param id the ID of the fax for which forwarding addresses are to be retrieved
     * @return {@link InboundFaxesEmailsStructure}
     * @throws UnsuccessfulStatusCodeException
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2930">https://www.interfax.net/en/dev/rest/reference/2930</a>
     */
    public InboundFaxesEmailsStructure getInboundFaxForwardingEmails(final String id)
            throws UnsuccessfulStatusCodeException;

    /**
     * Mark a transaction as read/unread
     *
     * @param id the ID of the fax to be marked
     * @param unread FALSE = mark as read, TRUE = mark as unread.
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2936">https://www.interfax.net/en/dev/rest/reference/2936</a>
     */
    public APIResponse markInboundFax(final String id, final Optional<Boolean> unread);

    /**
     * Resend an inbound fax to a specific email address
     *
     * @param id the ID of the fax to be resent by email
     * @param email email address to which to forward the inbound fax
     * @return {@link APIResponse}
     * @see <a href="https://www.interfax.net/en/dev/rest/reference/2929">https://www.interfax.net/en/dev/rest/reference/2929</a>
     */
    public APIResponse resendInboundFax(final String id, final Optional<String> email);

    // client lifecycle

    /**
     * Close underlying client and free up any held system resources
     */
    public void closeClient();

}
