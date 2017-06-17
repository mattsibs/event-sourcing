package com.event.sourcing.service;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.annotation.EventfulService;

@EventfulService
public class UserService implements EventfulProxy {

    @Eventful
    @Override
    public String testMethod() {
        return "Hello";
    }

}
