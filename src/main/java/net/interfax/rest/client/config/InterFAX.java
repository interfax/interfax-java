package net.interfax.rest.client.config;

public class InterFAX {

    private String outboundFaxesEndpoint;

    private String apiUsername;

    private String apiPassword;

    public String getOutboundFaxesEndpoint() {
        return outboundFaxesEndpoint;
    }

    public void setOutboundFaxesEndpoint(final String outboundFaxesEndpoint) {
        this.outboundFaxesEndpoint = outboundFaxesEndpoint;
    }

    public String getApiUsername() {
        return apiUsername;
    }

    public void setApiUsername(final String apiUsername) {
        this.apiUsername = apiUsername;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    public void setApiPassword(final String apiPassword) {
        this.apiPassword = apiPassword;
    }
}
