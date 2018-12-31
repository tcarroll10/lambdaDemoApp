package com.company.app.exception;

public class AppFileWriteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AppFileWriteException() {
    }

    public AppFileWriteException(final String message) {
        super(message);
    }

    public AppFileWriteException(final Throwable cause) {
        super(cause);
    }

    public AppFileWriteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AppFileWriteException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
