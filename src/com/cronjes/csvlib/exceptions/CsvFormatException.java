package com.cronjes.csvlib.exceptions;

public class CsvFormatException extends RuntimeException {

    public CsvFormatException() {
    }

    public CsvFormatException(String message) {
        super(message);
    }

    public CsvFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsvFormatException(Throwable cause) {
        super(cause);
    }

    public CsvFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
