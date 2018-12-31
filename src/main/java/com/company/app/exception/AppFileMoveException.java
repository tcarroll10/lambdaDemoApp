package com.company.app.exception;

public class AppFileMoveException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AppFileMoveException() {
    }

    public AppFileMoveException(final String message) {
        super(message);
    }

    public AppFileMoveException(final Throwable cause) {
        super(cause);
    }

    public AppFileMoveException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AppFileMoveException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
