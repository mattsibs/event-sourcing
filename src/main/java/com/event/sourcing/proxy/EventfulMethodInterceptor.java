package com.event.sourcing.proxy;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.event.EventService;
import com.google.common.collect.ImmutableList;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class EventfulMethodInterceptor implements MethodInterceptor {

    private final EventService eventService;
    private final PlatformTransactionManager transactionManager;

    public EventfulMethodInterceptor(final EventService eventService,
                                     final PlatformTransactionManager transactionManager) {
        this.eventService = eventService;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        List<Annotation> annotations = ImmutableList.copyOf(method.getDeclaredAnnotationsByType(Eventful.class));

        if (!annotations.isEmpty()) {
            return commit(invocation::proceed);
        }

        return invocation.proceed();
    }

    private <T> T commit(final TransactionalSupplier<T> transaction) throws Throwable {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            T result = transaction.run();
            eventService.persistLog(result);
            transactionManager.commit(status);
            return result;

        } catch (Throwable e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    @FunctionalInterface
    interface TransactionalSupplier<T> {
        T run() throws Throwable;
    }
}
