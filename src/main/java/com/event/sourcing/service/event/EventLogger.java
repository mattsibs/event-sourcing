package com.event.sourcing.service.event;

import com.event.sourcing.event.EventPayload;

@FunctionalInterface
public interface EventLogger {
    void logEvent(final EventPayload object);
}
