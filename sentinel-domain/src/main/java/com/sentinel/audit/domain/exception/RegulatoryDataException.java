package com.sentinel.audit.domain.exception;

public class RegulatoryDataException extends RuntimeException {
    private final String source;

    public RegulatoryDataException(String message, String source) {
        super(message);
        this.source = source;
    }

    public RegulatoryDataException(String message, Throwable cause, String source) {
        super(message, cause);
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
