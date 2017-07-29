package com.event.sourcing.service.event;

import com.event.sourcing.event.Event;

public abstract class EventReader<SOURCE> {

    public abstract void read(final EventHandler eventHandler);

    protected abstract Event marshallEvent(final SOURCE source);

}
