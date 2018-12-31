package com.company.app.exception;

public class AppFileReadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AppFileReadException() {
    }

    public AppFileReadException(final String message) {
        super(message);
    }

    public AppFileReadException(final Throwable cause) {
        super(cause);
    }

    public AppFileReadException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AppFileReadException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
