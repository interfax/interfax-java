package net.interfax.rest.client.impl;

import net.interfax.rest.client.InterFAXClient;
import net.interfax.rest.client.config.ClientConfig;
import net.interfax.rest.client.config.ClientCredentials;
import net.interfax.rest.client.config.ConfigLoader;
import net.interfax.rest.client.domain.APIResponse;
import net.interfax.rest.client.util.ArrayUtil;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
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
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InterFAXJerseyClient implements InterFAXClient {

    private static String username;
    private static String password;
    private static String scheme;
    private static String hostname;
    private static String outboundFaxesEndpoint;
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

        Response response = null;
        APIResponse apiResponse = null;

        try {

            String contentType = tika.detect(fileToSendAsFax);

            URI outboundFaxesUri = UriBuilder.fromUri(outboundFaxesEndpoint).queryParam("faxNumber", faxNumber).build();
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
            if (response != null)
                response.close();
        }

        return apiResponse;
    }

    @Override
    public APIResponse sendFax(final String faxNumber, final File[] filesToSendAsFax) {

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

            URI outboundFaxesUri = UriBuilder.fromUri(outboundFaxesEndpoint).queryParam("faxNumber", faxNumber).build();
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
            if (response != null)
                response.close();
        }

        return apiResponse;
    }

    @Override
    public APIResponse uploadDocument(final File fileToUpload) {

        Response response = null;
        APIResponse apiResponse = new APIResponse();

        try {

            // create document upload session
            URI outboundDocumentsUri = UriBuilder
                    .fromPath(outboundDocumentsEndpoint)
                    .scheme(scheme)
                    .host(hostname)
                    .port(8089)
                    .queryParam("size", fileToUpload.length())
                    .queryParam("name", fileToUpload.getName())
                    .build();

            WebTarget target = client.target(outboundDocumentsUri);
            response = target
                        .request()
                        .header("Content-Length", 0)
                        .post(null);

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
                for (int i=0; i<chunks.length; i++) {

                    boolean lastChunk = false;
                    if (i == chunks.length-1) {
                        lastChunk = true;
                    }
                    apiResponse = uploadChunk(uploadChunkToDocumentEndpoint, chunks[i], bytesUploaded, bytesUploaded+chunks[i].length-1, lastChunk);
                    bytesUploaded += chunks[i].length;
                }
            }

        } catch (Exception e) {
            log.error("Exception occurred while sending fax", e);
            apiResponse.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        } finally {
            if (response != null)
                response.close();
        }

        return apiResponse;
    }

    @Override
    public APIResponse uploadChunk(
            String uploadChunkToDocumentEndpoint,
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
                    .port(8089)
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
            log.error("Exception occurred while sending fax", e);
            apiResponse.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        } finally {
            if (response != null)
                response.close();
        }

        return apiResponse;
    }

    @Override
    public void closeClient() {

        client.close();
    }

    private void copyHeadersToAPIResponse(Response response, APIResponse apiResponse) {

        Map<String, List<Object>> headers = new HashMap<>();
        response.getHeaders().forEach(headers::put);
        apiResponse.setHeaders(headers);
    }

    private void initialiseCredentials() {

        ClientCredentials clientCredentials = new ConfigLoader<>(ClientCredentials.class, "interfax-api-credentials.yaml").getTestConfig();

        username = clientCredentials.getUsername();
        password = clientCredentials.getPassword();
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

            // required for the document upload API, to set Content-Length header
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

            // for automatically deriving content type given a file
            tika = new Tika();

            // read config from yaml
            scheme = clientConfig.getInterFAX().getScheme();
            hostname = clientConfig.getInterFAX().getHostname();
            outboundFaxesEndpoint = clientConfig.getInterFAX().getOutboundFaxesEndpoint();
            outboundDocumentsEndpoint = clientConfig.getInterFAX().getOutboundDocumentsEndpoint();
        } finally {
            reentrantLock.unlock();
        }
    }
}
