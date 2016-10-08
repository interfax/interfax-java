package net.interfax.rest.client.impl;

import net.interfax.rest.client.InterFAXClient;
import net.interfax.rest.client.config.ClientConfig;
import net.interfax.rest.client.config.ClientCredentials;
import net.interfax.rest.client.config.ConfigLoader;
import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetFaxListOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.OutboundFaxStructure;
import net.interfax.rest.client.domain.SendFaxOptions;
import net.interfax.rest.client.domain.UploadedDocumentStatus;
import net.interfax.rest.client.exception.UnsuccessfulStatusCodeException;
import net.interfax.rest.client.util.ArrayUtil;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class InterFAXJerseyClient implements InterFAXClient {

    private static String username;
    private static String password;
    private static String scheme;
    private static String hostname;
    private static int port;
    private static String outboundFaxesEndpoint;
    private static String outboundFaxesCompletedEndpoint;
    private static String outboundFaxesRecordEndpoint;
    private static String outboundFaxImageEndpoint;
    private static String outboundFaxesCancelEndpoint;
    private static String outboundDocumentsEndpoint;
    private static Client client;
    private static Tika tika;

    private final ReentrantLock reentrantLock = new ReentrantLock();

    private static final Logger log = LoggerFactory.getLogger(InterFAXJerseyClient.class);

    public InterFAXJerseyClient(String username, String password) {

        InterFAXJerseyClient.username = username;
        InterFAXJerseyClient.password = password;

        initializeClient(username, password);
    }

    public InterFAXJerseyClient() {

        initialiseCredentials();
        initializeClient(username, password);
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax) {

        return sendFax(faxNumber, fileToSendAsFax, Optional.empty());
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final File fileToSendAsFax, final Optional<SendFaxOptions> options) {

        Response response = null;
        APIResponse apiResponse = null;

        try {

            String contentType = tika.detect(fileToSendAsFax);

            URI outboundFaxesUri = getSendFaxUri(faxNumber, options);

            WebTarget target = client.target(outboundFaxesUri);
            response = target
                    .request()
                    .header("Content-Type", contentType)
                    .post(Entity.entity(fileToSendAsFax, contentType));

            apiResponse = new APIResponse();
            apiResponse.setStatusCode(response.getStatus());
            copyHeadersToAPIResponse(response, apiResponse);
            if (response.hasEntity())
                apiResponse.setResponseBody(response.readEntity(String.class));

        } catch (Exception e) {
            log.error("Exception occurred while sending fax", e);
        } finally {
            close(response);
        }

        return apiResponse;
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final File[] filesToSendAsFax) {

        return sendFax(faxNumber, filesToSendAsFax, Optional.empty());
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final File[] filesToSendAsFax, final Optional<SendFaxOptions> options) {

        Response response = null;
        APIResponse apiResponse = null;

        try {

            MultiPart multiPart = new MultiPart();
            int count = 1;
            for (File file : filesToSendAsFax) {
                String contentType = tika.detect(file);
                String entityName = "file"+count++;
                FileDataBodyPart fileDataBodyPart = new FileDataBodyPart(entityName, file, MediaType.valueOf(contentType));
                multiPart.bodyPart(fileDataBodyPart);
            }

            URI outboundFaxesUri = getSendFaxUri(faxNumber, options);
            WebTarget target = client.target(outboundFaxesUri);
            target.register(MultiPartFeature.class);
            response = target
                    .request()
                    .header("Content-Type", "multipart/mixed")
                    .post(Entity.entity(multiPart, multiPart.getMediaType()));

            apiResponse = new APIResponse();
            apiResponse.setStatusCode(response.getStatus());
            copyHeadersToAPIResponse(response, apiResponse);
            if (response.hasEntity())
                apiResponse.setResponseBody(response.readEntity(String.class));

        } catch (Exception e) {
            log.error("Exception occurred while sending fax", e);
        } finally {
            close(response);
        }

        return apiResponse;
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final String urlOfDoc) {

        return sendFax(faxNumber, urlOfDoc, Optional.empty());
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final String urlOfDoc, final Optional<SendFaxOptions> options) {

        Response response = null;
        APIResponse apiResponse = null;

        try {

            URI outboundFaxesUri = getSendFaxUri(faxNumber, options);
            WebTarget target = client.target(outboundFaxesUri);
            response = target
                    .request()
                    .header("Content-Location", urlOfDoc)
                    .header("Content-Length", 0)
                    .post(null);

            apiResponse = new APIResponse();
            apiResponse.setStatusCode(response.getStatus());
            copyHeadersToAPIResponse(response, apiResponse);
            if (response.hasEntity())
                apiResponse.setResponseBody(response.readEntity(String.class));


        } catch (Exception e) {
            log.error("Exception occurred while sending fax", e);
        } finally {
            close(response);
        }

        return apiResponse;
    }

    @Override
    public OutboundFaxStructure[] getFaxList() {

        return getFaxList(Optional.empty());
    }

    @Override
    public OutboundFaxStructure[] getFaxList(final Optional<GetFaxListOptions> options) {

        Response response = null;
        OutboundFaxStructure[] outboundFaxStructures = null;
        try {

            UriBuilder outboundFaxesUriBuilder =
                    UriBuilder.fromPath(outboundFaxesEndpoint).host(hostname).scheme(scheme).port(port);

            if (options.isPresent()) {
                GetFaxListOptions reqOptions = options.orElse(null);
                reqOptions.getLastId().ifPresent(x -> outboundFaxesUriBuilder.queryParam("lastId", x));
                reqOptions.getLimit().ifPresent(x -> outboundFaxesUriBuilder.queryParam("limit", x));
                reqOptions.getSortOrder().ifPresent(x -> outboundFaxesUriBuilder.queryParam("sortOrder", x));
                reqOptions.getUserId().ifPresent(x -> outboundFaxesUriBuilder.queryParam("userId", x));
            }

            WebTarget target = client.target(outboundFaxesUriBuilder.build());
            response = target.request().get();
            outboundFaxStructures = response.readEntity(OutboundFaxStructure[].class);
        } catch (Exception e) {
            log.error("Exception occurred while getting fax list", e);
        } finally {
            close(response);
        }

        return outboundFaxStructures;
    }

    @Override
    public OutboundFaxStructure[] getCompletedFaxList(final String[] ids) {

        Response response = null;
        OutboundFaxStructure[] outboundFaxStructures = null;

        try {

            URI outboundFaxesCompletedUri = UriBuilder
                                                .fromPath(outboundFaxesCompletedEndpoint)
                                                .host(hostname)
                                                .scheme(scheme)
                                                .port(port)
                                                .queryParam("ids", getCsvIds(ids))
                                                .build();

            WebTarget target = client.target(outboundFaxesCompletedUri);
            response = target.request().get();
            outboundFaxStructures = response.readEntity(OutboundFaxStructure[].class);
        } catch (Exception e) {
            log.error("Exception occurred while getting completed fax list", e);
        } finally {
            close(response);
        }

        return outboundFaxStructures;
    }

    @Override
    public OutboundFaxStructure getFaxRecord(final String id) throws UnsuccessfulStatusCodeException {

        Response response = null;
        OutboundFaxStructure outboundFaxStructure = null;
        try {

            URI outboundFaxesRecordUri = UriBuilder
                                            .fromUri(String.format(outboundFaxesRecordEndpoint, id))
                                            .scheme(scheme)
                                            .host(hostname)
                                            .port(port)
                                            .build();

            WebTarget target = client.target(outboundFaxesRecordUri);
            response = target.request().get();

            if (response.getStatus() == 200) {
                outboundFaxStructure = response.readEntity(OutboundFaxStructure.class);
            } else {
                throw new UnsuccessfulStatusCodeException("Unsuccessful response from API", response.getStatus());
            }
        } finally {
            close(response);
        }

        return outboundFaxStructure;
    }

    @Override
    public byte[] getFaxImage(final String id) throws UnsuccessfulStatusCodeException {

        Response response = null;
        byte[] responseBytes = null;
        try {

            URI uri = UriBuilder
                        .fromPath(String.format(outboundFaxImageEndpoint, id))
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

    @Override
    public APIResponse cancelFax(final String id) {

        Response response = null;
        APIResponse apiResponse = new APIResponse();

        try {

            URI uri = UriBuilder
                        .fromPath(String.format(outboundFaxesCancelEndpoint, id))
                        .host(hostname)
                        .scheme(scheme)
                        .port(port)
                        .build();

            response = client.target(uri).request().header("Content-Length", 0).post(null);
            apiResponse.setStatusCode(response.getStatus());
            if (response.hasEntity())
                apiResponse.setResponseBody(response.readEntity(String.class));
            copyHeadersToAPIResponse(response, apiResponse);
        } catch (Exception e) {
            log.error("Exception occurred while cancelling fax", e);
            apiResponse.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        } finally {
            close(response);
        }

        return apiResponse;
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
    public UploadedDocumentStatus[] getUploadedDocumentsList() {

        return getUploadedDocumentsList(Optional.empty());
    }

    @Override
    public UploadedDocumentStatus[] getUploadedDocumentsList(final Optional<GetUploadedDocumentsListOptions> options) {

        Response response = null;
        UploadedDocumentStatus[] uploadedDocumentStatuses = null;

        try {

            URI outboundDocumentsUriToGetDocumentsList = getUploadedDocumentListUri(options);

            WebTarget target = client.target(outboundDocumentsUriToGetDocumentsList);
            response = target.request().get();

            uploadedDocumentStatuses = response.readEntity(UploadedDocumentStatus[].class);
        } catch (Exception e) {
            log.error("Exception occurred while getting uploaded doc list", e);
        } finally {
            close(response);
        }

        return uploadedDocumentStatuses;
    }

    @Override
    public UploadedDocumentStatus getUploadedDocumentStatus(String documentId) {

        Response response = null;
        UploadedDocumentStatus uploadedDocumentStatus = null;

        try {

            URI outboundDocumentUri = UriBuilder
                    .fromPath(outboundDocumentsEndpoint+"/"+documentId)
                    .scheme(scheme)
                    .host(hostname)
                    .port(port)
                    .build();

            WebTarget target = client.target(outboundDocumentUri);
            response = target.request().get();

            uploadedDocumentStatus = response.readEntity(UploadedDocumentStatus.class);
        } catch (Exception e) {
            log.error("Exception occurred while getting uploaded doc status", e);
        } finally {
            close(response);
        }

        return uploadedDocumentStatus;
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
    public void closeClient() {

        client.close();
    }

    private URI getSendFaxUri(final String faxNumber, final Optional<SendFaxOptions> options) {

        UriBuilder outboundFaxesUriBuilder = UriBuilder
                .fromPath(outboundFaxesEndpoint)
                .scheme(scheme)
                .host(hostname)
                .port(port)
                .queryParam("faxNumber", faxNumber);

        if (options.isPresent()) {
            SendFaxOptions reqOptions = options.orElse(null);
            reqOptions.getContact().ifPresent(x -> outboundFaxesUriBuilder.queryParam("contact", x));
            reqOptions.getCsid().ifPresent(x -> outboundFaxesUriBuilder.queryParam("csid", x));
            reqOptions.getFitToPage().ifPresent(x -> outboundFaxesUriBuilder.queryParam("fitToPage", x));
            reqOptions.getPageHeader().ifPresent(x -> outboundFaxesUriBuilder.queryParam("pageHeader", x));
            reqOptions.getPageOrientation().ifPresent(x -> outboundFaxesUriBuilder.queryParam("pageOrientation", x));
            reqOptions.getPageSize().ifPresent(x -> outboundFaxesUriBuilder.queryParam("pageSize", x));
            reqOptions.getPostponeTime().ifPresent(x -> outboundFaxesUriBuilder.queryParam("postponeTime", x));
            reqOptions.getReference().ifPresent(x -> outboundFaxesUriBuilder.queryParam("reference", x));
            reqOptions.getRendering().ifPresent(x -> outboundFaxesUriBuilder.queryParam("rendering", x));
            reqOptions.getReplyAddress().ifPresent(x -> outboundFaxesUriBuilder.queryParam("replyAddress", x));
            reqOptions.getResolution().ifPresent(x -> outboundFaxesUriBuilder.queryParam("resolution", x));
            reqOptions.getRetriesToPerform().ifPresent(x -> outboundFaxesUriBuilder.queryParam("retriesToPerform", x));
        }

        return outboundFaxesUriBuilder.build();
    }

    private URI getOutboundDocumentsUri(final File fileToUpload, final Optional<DocumentUploadSessionOptions> options) {

        DocumentUploadSessionOptions reqOptions = options.orElse(null);

        UriBuilder outboundDocumentsUriBuilder = UriBuilder
                .fromPath(outboundDocumentsEndpoint)
                .scheme(scheme)
                .host(hostname)
                .port(port)
                .queryParam("size", options.isPresent() ? reqOptions.getSize().orElse(fileToUpload.length()) : fileToUpload.length())
                .queryParam("name", options.isPresent() ? reqOptions.getName().orElse(fileToUpload.getName()) : fileToUpload.getName());

        if (options.isPresent()) {
            reqOptions.getDisposition().ifPresent(x -> outboundDocumentsUriBuilder.queryParam("disposition", x.toString()));
            reqOptions.getSharing().ifPresent(x -> outboundDocumentsUriBuilder.queryParam("sharing", x.toString()));
        }

        return outboundDocumentsUriBuilder.build();
    }

    private URI getUploadedDocumentListUri(final Optional<GetUploadedDocumentsListOptions> options) {

        GetUploadedDocumentsListOptions reqOptions = options.orElse(null);

        UriBuilder uriBuilder = UriBuilder
                                        .fromPath(outboundDocumentsEndpoint)
                                        .scheme(scheme)
                                        .host(hostname)
                                        .port(port);

        if (options.isPresent()) {
            reqOptions.getLimit().ifPresent(x -> uriBuilder.queryParam("limit", reqOptions.getLimit().get()));
            reqOptions.getOffset().ifPresent(x -> uriBuilder.queryParam("offset", reqOptions.getOffset().get()));
        }

        return uriBuilder.build();
    }

    private void copyHeadersToAPIResponse(Response response, APIResponse apiResponse) {

        Map<String, List<Object>> headers = new HashMap<>();
        response.getHeaders().forEach(headers::put);
        apiResponse.setHeaders(headers);
    }

    private String getCsvIds(final String[] ids) {

        return Arrays.stream(ids).reduce((x, y)  -> x + "," + y).get();
    }

    private void initialiseCredentials() {

        ClientCredentials clientCredentials = new ConfigLoader<>(ClientCredentials.class, "interfax-api-credentials.yaml").getTestConfig();

        username = clientCredentials.getUsername();
        password = clientCredentials.getPassword();
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

    private void initializeClient(String username, String password) {

        reentrantLock.lock();
        try {

            if (client != null)
                return;

            // build client
            ClientConfig clientConfig = new ConfigLoader<>(ClientConfig.class, "interfax-api-config.yaml").getTestConfig();
            HttpAuthenticationFeature httpAuthenticationFeature = HttpAuthenticationFeature.basic(username, password);
            client = ClientBuilder.newClient();
            client.register(httpAuthenticationFeature);
            client.register(MultiPartFeature.class);
            client.register(RequestEntityProcessing.CHUNKED);
            client.register(JacksonFeature.class);

            // required for the document upload API, to set Content-Length header
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

            // for automatically deriving content type given a file
            tika = new Tika();

            // read config from yaml
            scheme = clientConfig.getInterFAX().getScheme();
            hostname = clientConfig.getInterFAX().getHostname();
            port = clientConfig.getInterFAX().getPort();
            outboundFaxesEndpoint = clientConfig.getInterFAX().getOutboundFaxesEndpoint();
            outboundFaxesCompletedEndpoint = clientConfig.getInterFAX().getOutboundFaxesCompletedEndpoint();
            outboundFaxesRecordEndpoint = clientConfig.getInterFAX().getOutboundFaxesRecordEndpoint();
            outboundFaxImageEndpoint = clientConfig.getInterFAX().getOutboundFaxImageEndpoint();
            outboundFaxesCancelEndpoint = clientConfig.getInterFAX().getOutboundFaxesCancelEndpoint();
            outboundDocumentsEndpoint = clientConfig.getInterFAX().getOutboundDocumentsEndpoint();
        } finally {
            reentrantLock.unlock();
        }
    }
}