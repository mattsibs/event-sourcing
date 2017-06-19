package com.event.sourcing.proxy;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.event.EventService;
import com.google.common.collect.ImmutableList;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class EventfulMethodInterceptor implements MethodInterceptor {

    private final EventService eventService;

    public EventfulMethodInterceptor(final EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        List<Annotation> annotations = ImmutableList.copyOf(method.getDeclaredAnnotationsByType(Eventful.class));

        if (!annotations.isEmpty()) {
            Object result = invocation.proceed();

            eventService.persistLog(result);
            return result;
        }

        return invocation.proceed();
    }
}
