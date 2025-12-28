package com.mulemind.provider;

/**
 * Exception thrown by LLM providers
 */
public class LlmProviderException extends Exception {

    private final int statusCode;
    private final String provider;

    public LlmProviderException(String message, String provider) {
        super(message);
        this.statusCode = -1;
        this.provider = provider;
    }

    public LlmProviderException(String message, int statusCode, String provider) {
        super(message);
        this.statusCode = statusCode;
        this.provider = provider;
    }

    public LlmProviderException(String message, Throwable cause, String provider) {
        super(message, cause);
        this.statusCode = -1;
        this.provider = provider;
    }

    public LlmProviderException(String message, int statusCode, Throwable cause, String provider) {
        super(message, cause);
        this.statusCode = statusCode;
        this.provider = provider;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return "LlmProviderException{" +
                "provider='" + provider + '\'' +
                ", statusCode=" + statusCode +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
