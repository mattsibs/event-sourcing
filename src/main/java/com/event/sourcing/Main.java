package com.event.sourcing;

import com.event.sourcing.proxy.EventfulProxyInvocationHandler;
import com.event.sourcing.service.EventfulProxy;
import com.event.sourcing.service.UserService;

import java.lang.reflect.Proxy;

public class Main {

    public static void main(final String[] args) {

        UserService userService = new UserService();

        EventfulProxy proxy = (EventfulProxy) Proxy.newProxyInstance(EventfulProxyInvocationHandler.class.getClassLoader(),
                new Class[] { EventfulProxy.class },
                new EventfulProxyInvocationHandler(userService));


        proxy.testMethod();
    }
}
