package net.interfax.rest.client.config;

public class InterFAX {

    private String scheme;

    private String hostname;

    private String outboundFaxesEndpoint;

    private String outboundDocumentsEndpoint;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(final String scheme) {
        this.scheme = scheme;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public String getOutboundFaxesEndpoint() {
        return outboundFaxesEndpoint;
    }

    public void setOutboundFaxesEndpoint(final String outboundFaxesEndpoint) {
        this.outboundFaxesEndpoint = outboundFaxesEndpoint;
    }

    public String getOutboundDocumentsEndpoint() {
        return outboundDocumentsEndpoint;
    }

    public void setOutboundDocumentsEndpoint(final String outboundDocumentsEndpoint) {
        this.outboundDocumentsEndpoint = outboundDocumentsEndpoint;
    }
}
