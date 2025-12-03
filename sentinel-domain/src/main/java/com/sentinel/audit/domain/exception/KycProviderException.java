package com.sentinel.audit.domain.exception;

public class KycProviderException extends RuntimeException {
    private final String providerId;
    private final String errorCode;

    public KycProviderException(String message, String providerId, String errorCode) {
        super(message);
        this.providerId = providerId;
        this.errorCode = errorCode;
    }

    public KycProviderException(String message, Throwable cause, String providerId, String errorCode) {
        super(message, cause);
        this.providerId = providerId;
        this.errorCode = errorCode;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
