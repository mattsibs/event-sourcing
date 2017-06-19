package com.event.sourcing.event;

@FunctionalInterface
public interface EventLogger {

    void logEvent(final Object object);

}
