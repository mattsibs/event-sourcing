package com.event.sourcing.proxy;

import com.event.sourcing.annotation.Eventful;
import com.google.common.collect.ImmutableList;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class EventfulMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(final Object object, final Method method, final Object[] args,
                            final MethodProxy methodProxy) throws Throwable {

        List<Annotation> annotations = ImmutableList.copyOf(method.getDeclaredAnnotationsByType(Eventful.class));

        if (!annotations.isEmpty()) {

            Object result = methodProxy.invokeSuper(object, args);

            System.out.println(result);
            return result;
        }

        return methodProxy.invokeSuper(object, args);

    }
}
