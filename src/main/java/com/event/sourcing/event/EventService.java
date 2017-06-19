package com.event.sourcing.event;

public class EventService {

    private final EventLogger eventLogger;

    public EventService(final EventLogger eventLogger) {
        this.eventLogger = eventLogger;
    }

    public void persistLog(final Object object) {
        System.out.println("Persisting " + object.toString());
        eventLogger.logEvent(object);
    }

}
