package com.cronjes.csvlib.exceptions;

public class MethodTypeException extends RuntimeException {
    public MethodTypeException() {
    }

    public MethodTypeException(String message) {
        super(message);
    }

    public MethodTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodTypeException(Throwable cause) {
        super(cause);
    }

    public MethodTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
