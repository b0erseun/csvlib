package com.cronjes.csvlib.exceptions;

public class ClassDefinitionException extends RuntimeException {
    public ClassDefinitionException() {
    }

    public ClassDefinitionException(String message) {
        super(message);
    }

    public ClassDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassDefinitionException(Throwable cause) {
        super(cause);
    }

    public ClassDefinitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
