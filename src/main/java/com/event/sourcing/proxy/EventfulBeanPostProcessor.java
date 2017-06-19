package com.event.sourcing.proxy;

import com.event.sourcing.annotation.EventfulService;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;

public class EventfulBeanPostProcessor extends AbstractAutoProxyCreator {

    private final  EventfulMethodInterceptor eventfulMethodInterceptor;

    public EventfulBeanPostProcessor(final EventfulMethodInterceptor eventfulMethodInterceptor) {
        this.eventfulMethodInterceptor = eventfulMethodInterceptor;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(final Class<?> beanClass, final String beanName,
                                                    final TargetSource customTargetSource) throws BeansException {

        if (beanClass.isAnnotationPresent(EventfulService.class)) {
            return new Object[]{eventfulMethodInterceptor};
        }

        return DO_NOT_PROXY;
    }
}
