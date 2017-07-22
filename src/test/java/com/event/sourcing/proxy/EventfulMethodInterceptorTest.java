package com.event.sourcing.proxy;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.annotation.EventfulService;
import com.event.sourcing.event.EventPayload;
import com.event.sourcing.service.event.EventService;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.lang.reflect.Method;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventfulMethodInterceptorTest {

    @Mock
    private EventService eventService;

    @Mock
    private PlatformTransactionManager transactionManager;

    private EventfulMethodInterceptor eventfulMethodInterceptor;

    @Before
    public void setUp() throws Exception {
        eventfulMethodInterceptor = new EventfulMethodInterceptor(eventService, transactionManager);
    }

    @Test
    public void invoke_methodInvocationThrowsException_throwsSameException() throws Throwable {
        MethodInvocation invocation = mock(MethodInvocation.class);
        given(invocation.getMethod())
                .willReturn(TestEventfulClass.class.getMethod("testMethod"));

        given(transactionManager.getTransaction(any()))
                .willReturn(mock(TransactionStatus.class));

        given(invocation.proceed())
                .willThrow(new RuntimeException());

        assertThatThrownBy(() -> eventfulMethodInterceptor.invoke(invocation))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void invoke_invocationThrowsException_rollsBackTransaction() throws Throwable {
        MethodInvocation invocation = mock(MethodInvocation.class);
        given(invocation.getMethod())
                .willReturn(TestEventfulClass.class.getMethod("testMethod"));

        TransactionStatus transactionStatus = mock(TransactionStatus.class);
        given(transactionManager.getTransaction(any()))
                .willReturn(transactionStatus);

        given(invocation.proceed())
                .willThrow(new RuntimeException());

        assertThatThrownBy(() -> eventfulMethodInterceptor.invoke(invocation))
                .isInstanceOf(RuntimeException.class);

        verify(transactionManager).rollback(transactionStatus);
    }

    @EventfulService
    public static class TestEventfulClass {
        @Eventful
        public EventPayload testMethod() {
            return mock(EventPayload.class);
        }
    }

}