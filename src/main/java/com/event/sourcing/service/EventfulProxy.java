package com.event.sourcing.service;

import com.event.sourcing.annotation.Eventful;

public interface EventfulProxy {
    @Eventful
    String testMethod();
}
