package net.interfax.rest.client.exception;

public class UnsuccessfulStatusCodeException extends Exception {

    private int statusCode;
    private String responseBody;

    public UnsuccessfulStatusCodeException(int statusCode) {
        this.statusCode = statusCode;
    }

    public UnsuccessfulStatusCodeException(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public UnsuccessfulStatusCodeException(String message) {
        super(message);
    }

    public UnsuccessfulStatusCodeException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public UnsuccessfulStatusCodeException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public UnsuccessfulStatusCodeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnsuccessfulStatusCodeException(final Throwable cause) {
        super(cause);
    }

    public UnsuccessfulStatusCodeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

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

}
