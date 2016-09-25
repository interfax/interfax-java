package net.interfax.rest.client.impl;

import net.interfax.rest.client.InterFAXClient;
import net.interfax.rest.client.domain.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

public class InterFAXJerseyClient implements InterFAXClient {

    private static String username;
    private static String password;
    private static HttpAuthenticationFeature httpAuthenticationFeature;
    private static Client client;

    private final ReentrantLock reentrantLock = new ReentrantLock();

    private static final Logger log = LoggerFactory.getLogger(InterFAXJerseyClient.class);

    public InterFAXJerseyClient() {

        initializeClient();
    }

    public net.interfax.rest.client.domain.Response sendFax(final String faxNumber, final File fileToSendAsFax) {

        javax.ws.rs.core.Response response = null;
        Response apiResponse = null;

        try {

            final WebTarget target = client.target(String.format(OUTBOUND_FAXES_ENDPOINT, faxNumber));
            response = target
                            .request()
                            .header("Content-Type", "application/pdf")
                            .post(Entity.entity(fileToSendAsFax, "application/pdf"));

            apiResponse = new Response();
            apiResponse.setStatusCode(response.getStatus());

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

    public void closeClient() {

        client.close();
    }

    private void initializeClient() {

        reentrantLock.lock();
        try {

            if (client != null)
                return;

            username = System.getenv("interfax-api-username");
            password = System.getenv("interfax-api-password");
            httpAuthenticationFeature = HttpAuthenticationFeature.basic(username, password);
            client = ClientBuilder.newClient();
            client.register(httpAuthenticationFeature);
        } finally {
            reentrantLock.unlock();
        }
    }
}
