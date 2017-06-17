package com.event.sourcing.proxy;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.service.EventfulProxy;
import com.google.common.collect.ImmutableList;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class EventfulProxyInvocationHandler implements InvocationHandler {

    private final EventfulProxy eventfulProxy;

    public EventfulProxyInvocationHandler(final EventfulProxy eventfulProxy) {
        this.eventfulProxy = eventfulProxy;
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

        List<Annotation> annotations = ImmutableList.copyOf(method.getDeclaredAnnotationsByType(Eventful.class));

        if (!annotations.isEmpty()) {

            Object result = method.invoke(eventfulProxy, args);

            System.out.println(result);
            return result;
        }

        return method.invoke(eventfulProxy, args);
    }
}
