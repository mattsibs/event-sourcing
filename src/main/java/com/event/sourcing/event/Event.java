package com.event.sourcing.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Event {

    @JsonProperty
    private EventPayload eventPayload;

    @JsonProperty
    private long timestamp;

    private Event() {

    }

    private Event(final EventPayload eventPayload, final LocalDateTime timestamp) {
        this.eventPayload = eventPayload;
        this.timestamp = timestamp.toEpochSecond(ZoneOffset.UTC);
    }

    public static Event of(final EventPayload eventPayload, final LocalDateTime timestamp) {
        return new Event(eventPayload, timestamp);
    }

    public EventPayload getEventPayload() {
        return eventPayload;
    }

    @JsonIgnore
    public LocalDateTime getTimestamp() {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }

}
