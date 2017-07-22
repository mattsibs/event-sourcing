package com.event.sourcing.service.event;

import com.event.sourcing.event.EventPayload;

public class EventService {

    private final EventLogger eventLogger;
    private final EventReader eventReader;
    private final EventHandler eventHandler;

    EventService(final EventLogger eventLogger, final EventReader eventReader, final EventHandler eventHandler) {
        this.eventLogger = eventLogger;
        this.eventReader = eventReader;
        this.eventHandler = eventHandler;
    }

    public void persistLog(final Object object) {
        eventLogger.logEvent((EventPayload) object);
    }

    public void updateEvents() {
        eventReader.read(eventHandler);
    }
}
