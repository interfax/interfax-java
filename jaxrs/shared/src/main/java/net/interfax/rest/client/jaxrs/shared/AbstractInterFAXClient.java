package net.interfax.rest.client.jaxrs.shared;

import net.interfax.rest.client.config.ClientConfig;
import net.interfax.rest.client.config.spi.UriEncoder;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.SendFaxOptions;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Optional;

public abstract class AbstractInterFAXClient {

    protected final UriEncoder uriEncoder;

    protected String scheme;
    protected String hostname;
    protected int port;
    protected String outboundFaxesEndpoint;
    protected String outboundFaxesCompletedEndpoint;
    protected String outboundFaxesRecordEndpoint;
    protected String outboundFaxImageEndpoint;
    protected String outboundFaxesCancelEndpoint;
    protected String outboundFaxesResendEndpoint;
    protected String outboundFaxesHideEndpoint;
    protected String outboundSearchEndpoint;
    protected String outboundDocumentsEndpoint;
    protected String accountsBalanceEndpoint;
    protected String inboundFaxesEndpoint;
    protected String inboundFaxesImageEndpoint;
    protected String inboundFaxesEmailsEndpoint;
    protected String inboundFaxesMarkEndpoint;
    protected String inboundFaxesResendEndpoint;

    public AbstractInterFAXClient(ClientConfig clientConfig) {
        readConfigAndInitializeEndpoints(clientConfig);
        this.uriEncoder = createUriEncoder();
    }

    protected URI getSendFaxUri(final String faxNumber, final Optional<SendFaxOptions> options)
            throws URISyntaxException {

        UriBuilder uriBuilder = UriBuilder.fromPath("").scheme(scheme).host(hostname).port(port).path(outboundFaxesEndpoint);
        uriBuilder.queryParam("faxNumber", faxNumber);

        param(uriBuilder, "contact", options.flatMap(SendFaxOptions::getContact));
        param(uriBuilder, "csid", options.flatMap(SendFaxOptions::getCsid));
        param(uriBuilder, "fitToPage", options.flatMap(SendFaxOptions::getFitToPage));
        param(uriBuilder, "pageHeader", options.flatMap(SendFaxOptions::getPageHeader));
        param(uriBuilder, "pageOrientation", options.flatMap(SendFaxOptions::getPageOrientation));
        param(uriBuilder, "pageSize", options.flatMap(SendFaxOptions::getPageSize));
        param(uriBuilder, "postponeTime", options.flatMap(SendFaxOptions::getPostponeTime));
        param(uriBuilder, "reference", options.flatMap(SendFaxOptions::getReference));
        param(uriBuilder, "rendering", options.flatMap(SendFaxOptions::getRendering));
        param(uriBuilder, "replyAddress", options.flatMap(SendFaxOptions::getReplyAddress));
        param(uriBuilder, "resolution", options.flatMap(SendFaxOptions::getResolution));
        param(uriBuilder, "retriesToPerform", options.flatMap(SendFaxOptions::getRetriesToPerform));

        return uriBuilder.build();
    }

    private void param(UriBuilder builder, String paramName, Optional<?> value) {
        value.map(Object::toString)
            .map(uriEncoder::encode)
            .ifPresent(encoded -> builder.queryParam(paramName, encoded));
    }

    protected URI getOutboundDocumentsUri(final File fileToUpload, final Optional<DocumentUploadSessionOptions> options) {

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

    protected URI getUploadedDocumentListUri(final Optional<GetUploadedDocumentsListOptions> options) {

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

    protected void readConfigAndInitializeEndpoints(ClientConfig clientConfig) {

        // read config from yaml
        scheme = clientConfig.getInterFAX().getScheme();
        hostname = clientConfig.getInterFAX().getHostname();
        port = clientConfig.getInterFAX().getPort();
        outboundFaxesEndpoint = clientConfig.getInterFAX().getOutboundFaxesEndpoint();
        outboundFaxesCompletedEndpoint = clientConfig.getInterFAX().getOutboundFaxesCompletedEndpoint();
        outboundFaxesRecordEndpoint = clientConfig.getInterFAX().getOutboundFaxesRecordEndpoint();
        outboundFaxImageEndpoint = clientConfig.getInterFAX().getOutboundFaxImageEndpoint();
        outboundFaxesCancelEndpoint = clientConfig.getInterFAX().getOutboundFaxesCancelEndpoint();
        outboundFaxesResendEndpoint = clientConfig.getInterFAX().getOutboundFaxesResendEndpoint();
        outboundFaxesHideEndpoint = clientConfig.getInterFAX().getOutboundFaxesHideEndpoint();
        outboundSearchEndpoint = clientConfig.getInterFAX().getOutboundSearchEndpoint();
        outboundDocumentsEndpoint = clientConfig.getInterFAX().getOutboundDocumentsEndpoint();
        accountsBalanceEndpoint = clientConfig.getInterFAX().getAccountsBalanceEndpoint();
        inboundFaxesEndpoint = clientConfig.getInterFAX().getInboundFaxesEndpoint();
        inboundFaxesImageEndpoint = clientConfig.getInterFAX().getInboundFaxesImageEndpoint();
        inboundFaxesEmailsEndpoint = clientConfig.getInterFAX().getInboundFaxesEmailsEndpoint();
        inboundFaxesMarkEndpoint = clientConfig.getInterFAX().getInboundFaxesMarkEndpoint();
        inboundFaxesResendEndpoint = clientConfig.getInterFAX().getInboundFaxesResendEndpoint();
    }

    protected abstract UriEncoder createUriEncoder();

}
