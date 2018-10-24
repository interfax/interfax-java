package net.interfax.rest.client.impl;

import net.interfax.rest.client.config.ClientConfig;
import net.interfax.rest.client.config.ClientCredentials;
import net.interfax.rest.client.config.ConfigLoader;
import net.interfax.rest.client.domain.DocumentUploadSessionOptions;
import net.interfax.rest.client.domain.GetUploadedDocumentsListOptions;
import net.interfax.rest.client.domain.SendFaxOptions;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractInterFAXClient {

    protected static String username;
    protected static String password;
    protected static String scheme;
    protected static String hostname;
    protected static int port;
    protected static String outboundFaxesEndpoint;
    protected static String outboundFaxesCompletedEndpoint;
    protected static String outboundFaxesRecordEndpoint;
    protected static String outboundFaxImageEndpoint;
    protected static String outboundFaxesCancelEndpoint;
    protected static String outboundFaxesResendEndpoint;
    protected static String outboundFaxesHideEndpoint;
    protected static String outboundSearchEndpoint;
    protected static String outboundDocumentsEndpoint;
    protected static String accountsBalanceEndpoint;
    protected static String inboundFaxesEndpoint;
    protected static String inboundFaxesImageEndpoint;
    protected static String inboundFaxesEmailsEndpoint;
    protected static String inboundFaxesMarkEndpoint;
    protected static String inboundFaxesResendEndpoint;


    protected URI getSendFaxUri(final String faxNumber, final Optional<SendFaxOptions> options)
            throws URISyntaxException {

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(hostname).setScheme(scheme).setPort(port).setPath(outboundFaxesEndpoint);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("faxNumber", faxNumber));

        if (options.isPresent()) {
            SendFaxOptions reqOptions = options.orElse(null);
            reqOptions.getContact().ifPresent(          x -> params.add(new BasicNameValuePair("contact", x)));
            reqOptions.getCsid().ifPresent(             x -> params.add(new BasicNameValuePair("csid", x)));
            reqOptions.getFitToPage().ifPresent(        x -> params.add(new BasicNameValuePair("fitToPage", x)));
            reqOptions.getPageHeader().ifPresent(       x -> params.add(new BasicNameValuePair("pageHeader", x)));
            reqOptions.getPageOrientation().ifPresent(  x -> params.add(new BasicNameValuePair("pageOrientation", x)));
            reqOptions.getPageSize().ifPresent(         x -> params.add(new BasicNameValuePair("pageSize", x)));
            reqOptions.getPostponeTime().ifPresent(     x -> params.add(new BasicNameValuePair("postponeTime", x.toString())));
            reqOptions.getReference().ifPresent(        x -> params.add(new BasicNameValuePair("reference", x)));
            reqOptions.getRendering().ifPresent(        x -> params.add(new BasicNameValuePair("rendering", x)));
            reqOptions.getReplyAddress().ifPresent(     x -> params.add(new BasicNameValuePair("replyAddress", x)));
            reqOptions.getResolution().ifPresent(       x -> params.add(new BasicNameValuePair("resolution", x)));
            reqOptions.getRetriesToPerform().ifPresent( x -> params.add(new BasicNameValuePair("retriesToPerform", String.valueOf(x))));
        }

        uriBuilder.setParameters(params);
        return uriBuilder.build();
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

    protected void initialiseCredentials() {

        ClientCredentials clientCredentials = new ConfigLoader<>(ClientCredentials.class, "interfax-api-credentials.yaml").getTestConfig();

        username = clientCredentials.getUsername();
        password = clientCredentials.getPassword();
    }

    protected void readConfigAndInitializeEndpoints(ClientConfig clientConfig) {

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
}
