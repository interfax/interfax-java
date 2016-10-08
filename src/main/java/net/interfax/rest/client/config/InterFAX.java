package net.interfax.rest.client.config;

public class InterFAX {

    private String scheme;

    private String hostname;

    private int port;

    private String outboundFaxesEndpoint;

    private String outboundFaxesCompletedEndpoint;

    private String outboundFaxesRecordEndpoint;

    private String outboundFaxImageEndpoint;

    private String outboundFaxesCancelEndpoint;

    private String outboundFaxesResendEndpoint;

    private String outboundFaxesHideEndpoint;

    private String outboundSearchEndpoint;

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

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getOutboundFaxesEndpoint() {
        return outboundFaxesEndpoint;
    }

    public void setOutboundFaxesEndpoint(final String outboundFaxesEndpoint) {
        this.outboundFaxesEndpoint = outboundFaxesEndpoint;
    }

    public String getOutboundFaxesCompletedEndpoint() {
        return outboundFaxesCompletedEndpoint;
    }

    public void setOutboundFaxesCompletedEndpoint(final String outboundFaxesCompletedEndpoint) {
        this.outboundFaxesCompletedEndpoint = outboundFaxesCompletedEndpoint;
    }

    public String getOutboundFaxesRecordEndpoint() {
        return outboundFaxesRecordEndpoint;
    }

    public void setOutboundFaxesRecordEndpoint(final String outboundFaxesRecordEndpoint) {
        this.outboundFaxesRecordEndpoint = outboundFaxesRecordEndpoint;
    }

    public String getOutboundFaxImageEndpoint() {
        return outboundFaxImageEndpoint;
    }

    public void setOutboundFaxImageEndpoint(final String outboundFaxImageEndpoint) {
        this.outboundFaxImageEndpoint = outboundFaxImageEndpoint;
    }

    public String getOutboundDocumentsEndpoint() {
        return outboundDocumentsEndpoint;
    }

    public void setOutboundDocumentsEndpoint(final String outboundDocumentsEndpoint) {
        this.outboundDocumentsEndpoint = outboundDocumentsEndpoint;
    }

    public String getOutboundFaxesCancelEndpoint() {
        return outboundFaxesCancelEndpoint;
    }

    public void setOutboundFaxesCancelEndpoint(final String outboundFaxesCancelEndpoint) {
        this.outboundFaxesCancelEndpoint = outboundFaxesCancelEndpoint;
    }

    public String getOutboundSearchEndpoint() {
        return outboundSearchEndpoint;
    }

    public void setOutboundSearchEndpoint(final String outboundSearchEndpoint) {
        this.outboundSearchEndpoint = outboundSearchEndpoint;
    }

    public String getOutboundFaxesResendEndpoint() {
        return outboundFaxesResendEndpoint;
    }

    public void setOutboundFaxesResendEndpoint(final String outboundFaxesResendEndpoint) {
        this.outboundFaxesResendEndpoint = outboundFaxesResendEndpoint;
    }

    public String getOutboundFaxesHideEndpoint() {
        return outboundFaxesHideEndpoint;
    }

    public void setOutboundFaxesHideEndpoint(final String outboundFaxesHideEndpoint) {
        this.outboundFaxesHideEndpoint = outboundFaxesHideEndpoint;
    }
}
