package net.interfax.rest.client.jaxrs.cxf;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class BasicInterceptor implements ClientRequestFilter {

    private final String credentials;

    public BasicInterceptor(String credentials) {
        this.credentials = credentials;
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        requestContext.getHeaders().add("Authorization", "Basic " + credentials);
    }
}
