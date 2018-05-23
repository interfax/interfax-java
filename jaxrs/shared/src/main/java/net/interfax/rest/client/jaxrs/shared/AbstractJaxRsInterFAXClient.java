package net.interfax.rest.client.jaxrs.shared;

import net.interfax.rest.client.InterFAX;
import net.interfax.rest.client.config.ClientConfig;
import net.interfax.rest.client.config.ClientCredentials;
import net.interfax.rest.client.config.ConfigLoader;
import net.interfax.rest.client.config.spi.ContentTypeDetector;
import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetFaxListOptions;
import net.interfax.rest.client.domain.GetInboundFaxListOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.InboundFaxStructure;
import net.interfax.rest.client.domain.InboundFaxesEmailsStructure;
import net.interfax.rest.client.domain.OutboundFaxStructure;
import net.interfax.rest.client.domain.SearchFaxOptions;
import net.interfax.rest.client.domain.SendFaxOptions;
import net.interfax.rest.client.domain.UploadedDocumentStatus;
import net.interfax.rest.client.exception.UnsuccessfulStatusCodeException;
import net.interfax.rest.client.util.ArrayUtil;
import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractJaxRsInterFAXClient<T extends FaxMultiPart> extends AbstractInterFAXClient implements InterFAX {

    private static final Logger log = LoggerFactory.getLogger(AbstractJaxRsInterFAXClient.class);

    private final Client client;

    private final ContentTypeDetector contentTypeDetector;

    public AbstractJaxRsInterFAXClient() {
        this(
            new ConfigLoader<>(ClientConfig.class, "interfax-api-config.yaml")
                .getTestConfig(),
            new ConfigLoader<>(ClientCredentials.class, "interfax-api-credentials.yaml")
                .getTestConfig()
        );
    }
    public AbstractJaxRsInterFAXClient(ClientCredentials clientCredentials) {
        this(
            new ConfigLoader<>(ClientConfig.class, "interfax-api-config.yaml")
                .getTestConfig(),
            clientCredentials
        );
    }

    public AbstractJaxRsInterFAXClient(ClientConfig clientConfig, ClientCredentials clientCredentials) {
        this(clientConfig, clientCredentials, new TikaContentTypeDetector());
    }

    public AbstractJaxRsInterFAXClient(ClientConfig clientConfig, ClientCredentials clientCredentials, ContentTypeDetector detector) {
        super(clientConfig);
        this.client = initializeClient(clientCredentials);
        this.contentTypeDetector = detector;
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax)
            throws IOException, URISyntaxException {

        return sendFax(faxNumber, fileToSendAsFax, Optional.empty());
    }

    @Override
    public APIResponse sendFax(final String faxNumber,
                               final File fileToSendAsFax,
                               final Optional<SendFaxOptions> options) throws IOException, URISyntaxException {

        String contentType = contentTypeDetector.detect(fileToSendAsFax);
        URI uri = getSendFaxUri(faxNumber, options);

        return executePostRequest(
                uri,
                target ->
                        target
                                .request()
                                .header("Content-Type", contentType)
                                .post(Entity.entity(fileToSendAsFax, contentType))
        );
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final File[] filesToSendAsFax)
            throws IOException, URISyntaxException {

        return sendFax(faxNumber, filesToSendAsFax, Optional.empty());
    }

	@Override
	public APIResponse sendFax(String faxNumber, InputStream[] streamsToSendAsFax, String[] fileNames)
            throws IOException, URISyntaxException {
		return sendFax(faxNumber, streamsToSendAsFax, fileNames, Optional.empty());
	}

    @Override
    public APIResponse sendFax(final String faxNumber,
                               final File[] filesToSendAsFax,
                               final Optional<SendFaxOptions> options) throws IOException, URISyntaxException {

        FaxMultiPart multiPart = createMultiPart();
        int count = 1;
        for (File file : filesToSendAsFax) {
            String contentType = contentTypeDetector.detect(file);
            String entityName = "file"+count++;
            multiPart.add(entityName, file, MediaType.valueOf(contentType));
        }

        return sendMultiPartFax(faxNumber, multiPart, options);
    }

    @Override
	public APIResponse sendFax(String faxNumber,
                               InputStream[] streamsToSendAsFax,
                               String[] mediaTypes,
                               Optional<SendFaxOptions> options) throws IOException, URISyntaxException {

		if (streamsToSendAsFax.length != mediaTypes.length) {
			throw new IllegalArgumentException("Stream and file name arrays do not have the same length");
		}

        FaxMultiPart multiPart = createMultiPart();
        for (int i=0; i < streamsToSendAsFax.length; i++) {
            final String entityName = "file"+i;
            multiPart.add(entityName, streamsToSendAsFax[i], MediaType.valueOf(mediaTypes[i]));
        }

        return sendMultiPartFax(faxNumber, multiPart, options);
	}

    @Override
    public APIResponse sendFax(final String faxNumber, final String urlOfDoc) throws URISyntaxException {

        return sendFax(faxNumber, urlOfDoc, Optional.empty());
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final String urlOfDoc, final Optional<SendFaxOptions> options)
            throws URISyntaxException {

        URI uri = getSendFaxUri(faxNumber, options);
        return executePostRequest(
                uri,
                target -> target.request().header("Content-Location", urlOfDoc).header("Content-Length", 0).post(null)
        );
    }

    @Override
    public APIResponse resendFax(final String id, final Optional<String> faxNumber) {

        UriBuilder outboundFaxesResendUriBuilder = UriBuilder
                                        .fromPath(String.format(outboundFaxesResendEndpoint, id))
                                        .scheme(scheme)
                                        .host(hostname)
                                        .port(port);

        faxNumber.ifPresent(x -> outboundFaxesResendUriBuilder.queryParam("faxNumber", x));
        URI uri = outboundFaxesResendUriBuilder.build();
        return executePostRequest(uri, target -> target.request().header("Content-Length", 0).post(null));
    }

    @Override
    public APIResponse hideFax(final String id) {

        String endpoint = String.format(outboundFaxesHideEndpoint, id);
        URI uri = UriBuilder.fromPath(endpoint).scheme(scheme).host(hostname).port(port).build();
        return executePostRequest(uri, target -> target.request().header("Content-Length", 0).post(null));
    }

    @Override
    public OutboundFaxStructure[] getFaxList() throws UnsuccessfulStatusCodeException {

        return getFaxList(Optional.empty());
    }

    @Override
    public OutboundFaxStructure[] getFaxList(final Optional<GetFaxListOptions> options)
            throws UnsuccessfulStatusCodeException {

        UriBuilder outboundFaxesUriBuilder =
                UriBuilder.fromPath(outboundFaxesEndpoint).host(hostname).scheme(scheme).port(port);

        if (options.isPresent()) {
            GetFaxListOptions reqOptions = options.orElse(null);
            reqOptions.getLastId().ifPresent(x -> outboundFaxesUriBuilder.queryParam("lastId", x));
            reqOptions.getLimit().ifPresent(x -> outboundFaxesUriBuilder.queryParam("limit", x));
            reqOptions.getSortOrder().ifPresent(x -> outboundFaxesUriBuilder.queryParam("sortOrder", x));
            reqOptions.getUserId().ifPresent(x -> outboundFaxesUriBuilder.queryParam("userId", x));
        }

        return (OutboundFaxStructure[]) executeGetRequest(
                outboundFaxesUriBuilder.build(),
                OutboundFaxStructure[].class,
                target -> target.request().get()
        );
    }

    @Override
    public OutboundFaxStructure[] getCompletedFaxList(final String[] ids) throws UnsuccessfulStatusCodeException {

        URI outboundFaxesCompletedUri = UriBuilder
                                            .fromPath(outboundFaxesCompletedEndpoint)
                                            .host(hostname)
                                            .scheme(scheme)
                                            .port(port)
                                            .queryParam("ids", uriEncoder.encode(getCsvIds(ids)))
                                            .build();
        return (OutboundFaxStructure[]) executeGetRequest(
                outboundFaxesCompletedUri,
                OutboundFaxStructure[].class,
                target -> target.request().get()
        );
    }

    @Override
    public OutboundFaxStructure getOutboundFaxRecord(final String id) throws UnsuccessfulStatusCodeException {

        URI outboundFaxesRecordUri = UriBuilder
                                        .fromUri(String.format(outboundFaxesRecordEndpoint, id))
                                        .scheme(scheme)
                                        .host(hostname)
                                        .port(port)
                                        .build();

        return (OutboundFaxStructure) executeGetRequest(
                outboundFaxesRecordUri,
                OutboundFaxStructure.class,
                target -> target.request().get()
        );
    }

    @Override
    public byte[] getOutboundFaxImage(final String id) throws UnsuccessfulStatusCodeException {

        return getFaxImage(String.format(outboundFaxImageEndpoint, id));
    }

    @Override
    public APIResponse cancelFax(final String id) {

        URI uri = UriBuilder
                    .fromPath(String.format(outboundFaxesCancelEndpoint, id))
                    .host(hostname)
                    .scheme(scheme)
                    .port(port)
                    .build();

        return executePostRequest(uri, target -> target.request().header("Content-Length", 0).post(null));
    }

    @Override
    public OutboundFaxStructure[] searchFaxList() throws UnsuccessfulStatusCodeException {

        URI uri = UriBuilder.fromUri(outboundSearchEndpoint).scheme(scheme).host(hostname).port(port).build();
        return  (OutboundFaxStructure[]) executeGetRequest(
                                            uri,
                                            OutboundFaxStructure[].class,
                                            target -> target.request().get()
        );
    }

    @Override
    public OutboundFaxStructure[] searchFaxList(final Optional<SearchFaxOptions> options)
            throws UnsuccessfulStatusCodeException {

        UriBuilder uriBuilder = UriBuilder.fromUri(outboundSearchEndpoint).scheme(scheme).host(hostname).port(port);
        if (options.isPresent()) {
            options.get().getIds().ifPresent(x -> uriBuilder.queryParam("ids", x));
            options.get().getReference().ifPresent(x -> uriBuilder.queryParam("reference", x));
            options.get().getDateFrom().ifPresent(x -> uriBuilder.queryParam("dateFrom", x));
            options.get().getDateTo().ifPresent(x -> uriBuilder.queryParam("dateTo", x));
            options.get().getStatus().ifPresent(x -> uriBuilder.queryParam("status", x));
            options.get().getUserId().ifPresent(x -> uriBuilder.queryParam("userId", x));
            options.get().getFaxNumber().ifPresent(x -> uriBuilder.queryParam("faxNumber", x));
            options.get().getLimit().ifPresent(x -> uriBuilder.queryParam("limit", x));
            options.get().getOffset().ifPresent(x -> uriBuilder.queryParam("offset", x));
        }

        return  (OutboundFaxStructure[]) executeGetRequest(
                uriBuilder.build(),
                OutboundFaxStructure[].class,
                target -> target.request().get()
        );
    }

    @Override
    public APIResponse uploadDocument(final File fileToUpload) {

        return uploadDocument(fileToUpload, Optional.empty());
    }

    @Override
    public APIResponse uploadDocument(final File fileToUpload, final Optional<DocumentUploadSessionOptions> options) {

        Response response = null;
        APIResponse apiResponse = new APIResponse();

        try {

            // create document upload session
            URI outboundDocumentsUri = getOutboundDocumentsUri(fileToUpload, options);

            WebTarget target = client.target(outboundDocumentsUri);
            response = target.request().header("Content-Length", 0).post(null);

            apiResponse = new APIResponse();
            apiResponse.setStatusCode(response.getStatus());
            copyHeadersToAPIResponse(response, apiResponse);

            if (response.hasEntity())
                apiResponse.setResponseBody(response.readEntity(String.class));

            // upload chunks
            if (apiResponse.getStatusCode() == Response.Status.CREATED.getStatusCode()) {

                String uploadChunkToDocumentEndpoint = URI
                                                    .create(apiResponse.getHeaders().get("Location").get(0).toString())
                                                    .getPath();

                InputStream inputStream = new FileInputStream(fileToUpload);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                int chunkSize = 1024*1024;
                byte[][] chunks = ArrayUtil.chunkArray(bytes, chunkSize);
                int bytesUploaded = 0;
                APIResponse chunkUploadResponses = new APIResponse();
                for (int i=0; i<chunks.length; i++) {

                    boolean lastChunk = false;
                    if (i == chunks.length-1) {
                        lastChunk = true;
                    }
                    chunkUploadResponses = uploadChunk(uploadChunkToDocumentEndpoint, chunks[i], bytesUploaded, bytesUploaded+chunks[i].length-1, lastChunk);
                    bytesUploaded += chunks[i].length;
                }
                apiResponse.setStatusCode(chunkUploadResponses.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Exception occurred while uplading document", e);
            apiResponse.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        } finally {
            close(response);
        }

        return apiResponse;
    }

    @Override
    public APIResponse uploadChunk(String uploadChunkToDocumentEndpoint,
                                   byte[] bytesToUpload,
                                   int startByteRange,
                                   int endByteRange,
                                   boolean lastChunk) {

        Response response = null;
        APIResponse apiResponse = new APIResponse();

        try {
            URI uploadChunkToDocumentUri = UriBuilder
                    .fromPath(uploadChunkToDocumentEndpoint)
                    .scheme(scheme)
                    .host(hostname)
                    .port(port)
                    .build();

            WebTarget target = client.target(uploadChunkToDocumentUri);
            response = target
                    .request()
                    .header("Range", "bytes="+startByteRange+"-"+endByteRange)
                    .post(Entity.entity(bytesToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));

            int expectedResponseCode = lastChunk ?
                                        Response.Status.OK.getStatusCode():
                                        Response.Status.ACCEPTED.getStatusCode();

            if (response.getStatus() == expectedResponseCode) {
                log.info(
                        "chunk uploaded at {}; totalByesUploaded = {}; lastChunk = {}",
                        uploadChunkToDocumentEndpoint,
                        endByteRange,
                        lastChunk
                );
            } else {
                // TODO: define and use a custom exception
                throw new Exception("Unexpected response code when uploading chunk"+response.getStatus());
            }

            apiResponse.setStatusCode(response.getStatus());
            copyHeadersToAPIResponse(response, apiResponse);
        } catch (Exception e) {
            log.error("Exception occurred while uploading chunk", e);
            apiResponse.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        } finally {
            close(response);
        }

        return apiResponse;
    }

    @Override
    public UploadedDocumentStatus[] getUploadedDocumentsList() throws UnsuccessfulStatusCodeException {

        return getUploadedDocumentsList(Optional.empty());
    }

    @Override
    public UploadedDocumentStatus[] getUploadedDocumentsList(final Optional<GetUploadedDocumentsListOptions> options)
            throws UnsuccessfulStatusCodeException {

        URI outboundDocumentsUriToGetDocumentsList = getUploadedDocumentListUri(options);
        return (UploadedDocumentStatus[]) executeGetRequest(
                outboundDocumentsUriToGetDocumentsList,
                UploadedDocumentStatus[].class,
                target -> target.request().get()
        );
    }

    @Override
    public UploadedDocumentStatus getUploadedDocumentStatus(String documentId) throws UnsuccessfulStatusCodeException {

        URI outboundDocumentUri = UriBuilder
                .fromPath(outboundDocumentsEndpoint+"/"+documentId)
                .scheme(scheme)
                .host(hostname)
                .port(port)
                .build();

        return (UploadedDocumentStatus) executeGetRequest(
                outboundDocumentUri,
                UploadedDocumentStatus.class,
                target -> target.request().get());
    }

    @Override
    public APIResponse cancelDocumentUploadSession(final String documentId) {

        Response response = null;
        APIResponse apiResponse = new APIResponse();

        try {

            URI outboundDocumentUri = UriBuilder
                    .fromPath(outboundDocumentsEndpoint+"/"+documentId)
                    .scheme(scheme)
                    .host(hostname)
                    .port(port)
                    .build();

            WebTarget target = client.target(outboundDocumentUri);
            response = target.request().delete();

            apiResponse.setStatusCode(response.getStatus());
            apiResponse.setResponseBody(response.readEntity(String.class));
            copyHeadersToAPIResponse(response, apiResponse);
        } catch (Exception e) {
            log.error("Exception occurred while cancelling doc upload session", e);
        } finally {
            close(response);
        }

        return apiResponse;
    }

    @Override
    public Double getAccountCredits() throws UnsuccessfulStatusCodeException {

        URI uri = UriBuilder.fromPath(accountsBalanceEndpoint).scheme(scheme).host(hostname).port(port).build();
        return (Double) executeGetRequest(uri, Double.class, target -> target.request().get());
    }

    @Override
    public InboundFaxStructure[] getInboundFaxList() throws UnsuccessfulStatusCodeException {

        return getInboundFaxList(Optional.empty());
    }

    @Override
    public InboundFaxStructure[] getInboundFaxList(final Optional<GetInboundFaxListOptions> options)
            throws UnsuccessfulStatusCodeException {

        UriBuilder uriBuilder = UriBuilder.fromPath(inboundFaxesEndpoint).scheme(scheme).host(hostname).port(port);
        if (options.isPresent()) {
            options.get().getAllUsers().ifPresent(x -> uriBuilder.queryParam("allUsers", x));
            options.get().getLastId().ifPresent(x -> uriBuilder.queryParam("lastId", x));
            options.get().getLimit().ifPresent(x -> uriBuilder.queryParam("limit", x));
            options.get().getUnreadOnly().ifPresent(x -> uriBuilder.queryParam("unreadOnly", x));
        }

        return (InboundFaxStructure[])
                executeGetRequest(uriBuilder.build(), InboundFaxStructure[].class, t -> t.request().get());
    }

    @Override
    public InboundFaxStructure getInboundFaxRecord(final String id) throws UnsuccessfulStatusCodeException {

        String path = inboundFaxesEndpoint+"/"+id;
        URI uri = UriBuilder.fromPath(path).scheme(scheme).host(hostname).port(port).build();
        return (InboundFaxStructure) executeGetRequest(uri, InboundFaxStructure.class, t -> t.request().get());
    }

    @Override
    public byte[] getInboundFaxImage(final long id) throws UnsuccessfulStatusCodeException {

        return getFaxImage(String.format(inboundFaxesImageEndpoint, id));
    }

    @Override
    public InboundFaxesEmailsStructure getInboundFaxForwardingEmails(final String id)
            throws UnsuccessfulStatusCodeException {

        String path = String.format(inboundFaxesEmailsEndpoint, id);
        URI uri = UriBuilder.fromPath(path).scheme(scheme).host(hostname).port(port).build();
        return (InboundFaxesEmailsStructure)
                executeGetRequest(uri, InboundFaxesEmailsStructure.class, t -> t.request().get());
    }

    @Override
    public APIResponse markInboundFax(final String id, final Optional<Boolean> unread) {

        String path = String.format(inboundFaxesMarkEndpoint, id);
        UriBuilder uriBuilder = UriBuilder.fromPath(path).scheme(scheme).host(hostname).port(port);
        unread.ifPresent(x -> uriBuilder.queryParam("unread", x));
        return executePostRequest(uriBuilder.build(), t -> t.request().header("Content-Length", 0).post(null));
    }

    @Override
    public APIResponse resendInboundFax(final String id, final Optional<String> email) {

        String path = String.format(inboundFaxesResendEndpoint, id);
        UriBuilder uriBuilder = UriBuilder.fromPath(path).scheme(scheme).host(hostname).port(port);
        email.ifPresent(x -> uriBuilder.queryParam("email", x));
        return executePostRequest(uriBuilder.build(), t -> t.request().header("Content-Length", 0).post(null));
    }

    @Override
    public void closeClient() {

        client.close();
    }

    private APIResponse sendMultiPartFax(String faxNumber, FaxMultiPart multiPart, Optional<SendFaxOptions> options)
            throws URISyntaxException {

        final URI uri = getSendFaxUri(faxNumber, options);
        return executePostRequest(
                uri,
                (target) ->
                        target
                                .request()
                                .header("Content-Type", "multipart/mixed")
                                .post(Entity.entity(multiPart.getMultiPart(), multiPart.getMediaType()))
        );
    }

    private APIResponse executePostRequest(URI uri, RequestExecutor executor) {

        Response response = null;
        APIResponse apiResponse = new APIResponse();

        try {

            WebTarget target = client.target(uri);
            response = executor.readyTheTargetAndExecute(target);
            apiResponse.setStatusCode(response.getStatus());
            copyHeadersToAPIResponse(response, apiResponse);
            if (response.hasEntity())
                apiResponse.setResponseBody(response.readEntity(String.class));

        } catch (Exception e) {
            log.error("Exception occurred while executing request", e);
            apiResponse.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            if (response != null && response.hasEntity())
                apiResponse.setResponseBody(response.readEntity(String.class));
        } finally {
            close(response);
        }

        return apiResponse;
    }

    private Object executeGetRequest(URI uri, Class responseEntityClass, RequestExecutor executor)
            throws UnsuccessfulStatusCodeException {

        Response response = null;
        Object responseEntity = null;

        try {

            WebTarget target = client.target(uri);
            response = executor.readyTheTargetAndExecute(target);
            if (response.getStatus() == Response.Status.OK.getStatusCode() && response.hasEntity()) {
                responseEntity = response.readEntity(responseEntityClass);
            }
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                String responseBody = null;
                if (response.hasEntity())
                    responseBody = response.readEntity(String.class);
                throw new UnsuccessfulStatusCodeException(response.getStatus(), responseBody);
            }

        } finally {
            close(response);
        }

        return responseEntity;
    }

    private byte[] getFaxImage(String path) throws UnsuccessfulStatusCodeException {

        Response response = null;
        byte[] responseBytes = null;
        try {

            URI uri = UriBuilder
                    .fromPath(path)
                    .host(hostname)
                    .scheme(scheme)
                    .port(port)
                    .build();

            response = client.target(uri).request().get();
            if (response.getStatus() == 200) {
                InputStream inputStream = response.readEntity(InputStream.class);
                responseBytes = IOUtils.toByteArray(inputStream);
                inputStream.close();
            } else {
                throw new UnsuccessfulStatusCodeException("Unsuccessful response from API", response.getStatus());
            }
        } catch (IOException e) {
            log.error("Exception occurred while getting fax image", e);
        } finally {
            close(response);
        }

        return responseBytes;
    }

    private void copyHeadersToAPIResponse(Response response, APIResponse apiResponse) {

        Map<String, List<Object>> headers = new HashMap<>();
        response.getHeaders().forEach(headers::put);
        apiResponse.setHeaders(headers);
    }

    private String getCsvIds(final String[] ids) {

        return Arrays.stream(ids).reduce((x, y)  -> x + "," + y).orElse("");
    }

    private void close(Response response) {

        if (response != null) {
            try {
                response.close();
            } catch (Exception e) {
                log.error("Failed to close response, may cause a connection leak", e);
            }
        }
    }

    protected abstract Client initializeClient(ClientCredentials clientCredentials);

    protected abstract FaxMultiPart createMultiPart();

}