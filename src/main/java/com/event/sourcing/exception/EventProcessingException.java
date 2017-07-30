package com.event.sourcing.exception;

public class EventProcessingException extends RuntimeException {
    public EventProcessingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
