package com.event.sourcing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Event<T> {

    private final T domainObject;

    @JsonProperty
    private final Type type;

    enum Type {
        INCEPTION,
        SUCCESS,
        FAILURE;
    }

    protected Event(final T domainObject, final Type type) {
        this.domainObject = domainObject;
        this.type = type;
    }

    public T getDomainObject() {
        return domainObject;
    }

    public Type getType() {
        return type;
    }
}
