package net.interfax.rest.client.domain;

import java.util.List;
import java.util.Map;

public class APIResponse {

    private int statusCode;
    private String responseBody;
    private Map<String, List<Object>> headers;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(final String responseBody) {
        this.responseBody = responseBody;
    }

    public Map<String, List<Object>> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, List<Object>> headers) {
        this.headers = headers;
    }
}
