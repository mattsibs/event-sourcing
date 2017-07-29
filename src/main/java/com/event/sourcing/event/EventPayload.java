package com.event.sourcing.event;

import com.event.sourcing.model.DataManager;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class EventPayload<T> {

    @JsonProperty
    private Class<T> domainObject;

    @JsonProperty
    private Type type;

    public enum Type {
        CREATE,
        UPDATE,
        DELETE;
    }

    protected EventPayload(final Class<T> domainObject, final Type type) {
        this.domainObject = domainObject;
        this.type = type;
    }

    public abstract T process(final DataManager dataManager);

    public Class<T> getDomainObject() {
        return domainObject;
    }

    public void setDomainObject(final Class<T> domainObject) {
        this.domainObject = domainObject;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }
}
