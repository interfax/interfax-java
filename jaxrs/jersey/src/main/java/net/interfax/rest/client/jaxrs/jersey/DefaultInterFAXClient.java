package net.interfax.rest.client.jaxrs.jersey;

import net.interfax.rest.client.jaxrs.jersey.multipart.JerseyMultiPart;
import net.interfax.rest.client.InterFAX;
import net.interfax.rest.client.config.ClientConfig;
import net.interfax.rest.client.config.ClientCredentials;
import net.interfax.rest.client.config.ConfigLoader;
import net.interfax.rest.client.config.spi.UriEncoder;
import net.interfax.rest.client.jaxrs.shared.AbstractJaxRsInterFAXClient;
import net.interfax.rest.client.jaxrs.shared.FaxMultiPart;
import net.interfax.rest.client.jaxrs.shared.JdkEncoder;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class DefaultInterFAXClient extends AbstractJaxRsInterFAXClient<JerseyMultiPart> implements InterFAX {

    public DefaultInterFAXClient() {
        this(
            new ConfigLoader<>(ClientConfig.class, "interfax-api-config.yaml")
                .getTestConfig(),
            new ConfigLoader<>(ClientCredentials.class, "interfax-api-credentials.yaml")
                .getTestConfig()
        );
    }
    public DefaultInterFAXClient(ClientCredentials clientCredentials) {
        this(
            new ConfigLoader<>(ClientConfig.class, "interfax-api-config.yaml")
                .getTestConfig(),
            clientCredentials
        );
    }

    public DefaultInterFAXClient(ClientConfig clientConfig, ClientCredentials clientCredentials) {
        super(clientConfig, clientCredentials);
    }

    @Override
    protected UriEncoder createUriEncoder() {
        return new JdkEncoder();
    }

    protected final Client initializeClient(ClientCredentials clientCredentials) {
        // build client
        HttpAuthenticationFeature httpAuthenticationFeature = HttpAuthenticationFeature.basic(clientCredentials.getUsername(),
            clientCredentials.getPassword());

        Client client = ClientBuilder.newClient();
        client.register(httpAuthenticationFeature);
        client.register(MultiPartFeature.class);
        client.register(RequestEntityProcessing.CHUNKED);
        client.register(JacksonFeature.class);

        // required for the document upload API, to set Content-Length header
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        return client;
    }

    @Override
    protected FaxMultiPart createMultiPart() {
        return new JerseyMultiPart();
    }

}