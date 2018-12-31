package com.company.app.exception;

public class AppFileFindException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AppFileFindException() {
    }

    public AppFileFindException(final String message) {
        super(message);
    }

    public AppFileFindException(final Throwable cause) {
        super(cause);
    }

    public AppFileFindException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AppFileFindException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
