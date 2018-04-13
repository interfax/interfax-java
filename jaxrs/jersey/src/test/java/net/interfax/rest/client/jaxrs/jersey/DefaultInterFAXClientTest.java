package net.interfax.rest.client.jaxrs.jersey;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import net.interfax.rest.client.InterFAX;
import net.interfax.rest.client.test.InterFAXClientTest;

public class DefaultInterFAXClientTest extends InterFAXClientTest {

    @Override
    protected InterFAX createClient() {
        return new DefaultInterFAXClient();
    }

    @Override
    protected WireMockConfiguration customize(WireMockConfiguration configuration) {
        return configuration.usingFilesUnderDirectory("../test/src/main/resources");
    }

}