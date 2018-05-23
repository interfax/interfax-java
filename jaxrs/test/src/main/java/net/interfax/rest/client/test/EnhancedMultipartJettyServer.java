package net.interfax.rest.client.test;

import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.http.AdminRequestHandler;
import com.github.tomakehurst.wiremock.http.HttpServer;
import com.github.tomakehurst.wiremock.http.HttpServerFactory;
import com.github.tomakehurst.wiremock.http.StubRequestHandler;

public class EnhancedMultipartJettyServer implements HttpServerFactory {

    @Override
    public HttpServer buildHttpServer(
        Options options,
        AdminRequestHandler adminRequestHandler,
        StubRequestHandler stubRequestHandler
    ) {
        return new EnhancedMultipartJettyHttpServer(
            options,
            adminRequestHandler,
            stubRequestHandler
        );
    }

}
