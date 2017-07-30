package com.event.sourcing.service.event;

import com.event.sourcing.event.Event;

@FunctionalInterface
public interface EventHandler {
    void handle(final Event event);
}
