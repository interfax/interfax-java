package net.interfax.rest.client.jaxrs.cxf;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import net.interfax.rest.client.InterFAX;
import net.interfax.rest.client.config.ClientConfig;
import net.interfax.rest.client.config.ClientCredentials;
import net.interfax.rest.client.config.ConfigLoader;
import net.interfax.rest.client.config.spi.ContentTypeDetector;
import net.interfax.rest.client.config.spi.UriEncoder;
import net.interfax.rest.client.jaxrs.cxf.multipart.AttachmentMultiPart;
import net.interfax.rest.client.jaxrs.shared.AbstractJaxRsInterFAXClient;
import net.interfax.rest.client.jaxrs.shared.FaxMultiPart;
import net.interfax.rest.client.jaxrs.shared.JdkEncoder;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.provider.MultipartProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class CxfClient extends AbstractJaxRsInterFAXClient<AttachmentMultiPart> implements InterFAX {

    public CxfClient() {
        this(
            new ConfigLoader<>(ClientConfig.class, "interfax-api-config.yaml")
                .getTestConfig(),
            new ConfigLoader<>(ClientCredentials.class, "interfax-api-credentials.yaml")
                .getTestConfig()
        );
    }
    public CxfClient(ClientCredentials clientCredentials) {
        this(
            new ConfigLoader<>(ClientConfig.class, "interfax-api-config.yaml")
                .getTestConfig(),
            clientCredentials
        );
    }

    public CxfClient(ClientConfig clientConfig, ClientCredentials clientCredentials) {
        super(clientConfig, clientCredentials);
    }

    public CxfClient(ClientConfig clientConfig, ClientCredentials clientCredentials, ContentTypeDetector detector) {
        super(clientConfig, clientCredentials, detector);
    }

    protected Client initializeClient(ClientCredentials clientCredentials) {
        // build client
        String credentials = Base64.getEncoder().encodeToString(
            (clientCredentials.getUsername() + ":" + clientCredentials.getPassword()).getBytes()
        );
        Client client = ClientBuilder.newClient();
        client.property("http.receive.timeout", TimeUnit.MINUTES.toMillis(1));
        client.register(new BasicInterceptor(credentials));
        client.register(new MultipartProvider());
        client.register(new JacksonJsonProvider());
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setLogBinary(false);
        loggingFeature.setLogMultipart(false);
        client.register(loggingFeature);

        // required for the document upload API, to set Content-Length header
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        return client;
    }

    @Override
    protected FaxMultiPart createMultiPart() {
        return new AttachmentMultiPart();
    }

    @Override
    protected UriEncoder createUriEncoder() {
        return new JdkEncoder();
    }

}