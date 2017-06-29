package com.event.sourcing.proxy;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.annotation.EventfulService;
import com.event.sourcing.config.SpringServiceConfiguration;
import com.event.sourcing.event.Event;
import com.event.sourcing.service.role.RoleService;
import org.apache.log4j.Logger;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;

import java.lang.reflect.Method;

public class EventfulBeanPostProcessor extends AbstractAutoProxyCreator {
    private static final Logger L = Logger.getLogger(EventfulBeanPostProcessor.class);

    private final  EventfulMethodInterceptor eventfulMethodInterceptor;

    public EventfulBeanPostProcessor(final EventfulMethodInterceptor eventfulMethodInterceptor) {
        this.eventfulMethodInterceptor = eventfulMethodInterceptor;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(final Class<?> beanClass, final String beanName,
                                                    final TargetSource customTargetSource) throws BeansException {

        if (beanClass.isAnnotationPresent(EventfulService.class)) {
            validateEventfulService(beanClass);
            L.info("Adding eventful proxy for class " + beanClass);
            return new Object[]{eventfulMethodInterceptor};
        }

        return DO_NOT_PROXY;
    }

    private void validateEventfulService(final Class<?> beanClass) {
        for (final Method method : beanClass.getMethods()) {
            if (method.isAnnotationPresent(Eventful.class)
                    && !Event.class.isAssignableFrom(method.getReturnType())) {
                throw new IllegalStateException( String.format("", method.getName(), method.getReturnType()));
            }
        }
    }

}
