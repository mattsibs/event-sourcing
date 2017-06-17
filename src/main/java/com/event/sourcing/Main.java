package com.event.sourcing;

import com.event.sourcing.proxy.EventfulMethodInterceptor;
import com.event.sourcing.service.UserService;
import net.sf.cglib.proxy.Enhancer;

public class Main {

    public static void main(final String[] args) {

        UserService proxy = (UserService) Enhancer.create(UserService.class, new EventfulMethodInterceptor());

        proxy.testMethod();
    }
}
