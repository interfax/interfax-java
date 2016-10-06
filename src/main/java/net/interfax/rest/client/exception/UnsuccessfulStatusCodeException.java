package net.interfax.rest.client.exception;

public class UnsuccessfulStatusCodeException extends Exception {


    public UnsuccessfulStatusCodeException(String message) {
        super(message);
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

}
