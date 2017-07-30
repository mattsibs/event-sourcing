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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class EventfulMethodInterceptorTest {

    @Mock
    private EventService eventService;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private EventPayload eventPayload;

    private EventfulMethodInterceptor eventfulMethodInterceptor;

    @Before
    public void setUp() throws Exception {
        eventfulMethodInterceptor = new EventfulMethodInterceptor(eventService, transactionManager);
    }

    @Test
    public void invoke_eventfulAnnotationNotPresent_invokesMethod() throws Throwable {
        MethodInvocation methodInvocation = mock(MethodInvocation.class);
        Method nonEventfulMethod = TestEventfulService.class.getMethod("nonEventfulMethod");

        given(methodInvocation.getMethod())
                .willReturn(nonEventfulMethod);

        eventfulMethodInterceptor.invoke(methodInvocation);

        verify(methodInvocation).proceed();
    }

    @Test
    public void invoke_eventfulAnnotationNotPresent_doesNotCreateTransaction() throws Throwable {
        MethodInvocation methodInvocation = mock(MethodInvocation.class);
        Method nonEventfulMethod = TestEventfulService.class.getMethod("nonEventfulMethod");

        given(methodInvocation.getMethod())
                .willReturn(nonEventfulMethod);

        eventfulMethodInterceptor.invoke(methodInvocation);

        verify(transactionManager, never()).getTransaction(any());
        verifyZeroInteractions(transactionManager);
    }

    @Test
    public void invoke_eventfulMethodSuccessful_commitsTransaction() throws Throwable {
        MethodInvocation methodInvocation = mock(MethodInvocation.class);
        Method eventfulMethod = TestEventfulService.class.getMethod("eventfulMethod");

        given(methodInvocation.getMethod())
                .willReturn(eventfulMethod);

        TransactionStatus transactionStatus = mock(TransactionStatus.class);
        given(transactionManager.getTransaction(any()))
                .willReturn(transactionStatus);

        willDoNothing()
                .given(eventService).persistLog(anyObject());

        eventfulMethodInterceptor.invoke(methodInvocation);

        verify(transactionManager).commit(transactionStatus);
    }

    @Test
    public void invoke_eventfulMethodSuccessful_persistsLog() throws Throwable {
        MethodInvocation methodInvocation = mock(MethodInvocation.class);
        Method eventfulMethod = TestEventfulService.class.getMethod("eventfulMethod");
        EventPayload methodResult = mock(EventPayload.class);

        given(methodInvocation.getMethod())
                .willReturn(eventfulMethod);
        given(methodInvocation.proceed())
                .willReturn(methodResult);

        TransactionStatus transactionStatus = mock(TransactionStatus.class);
        given(transactionManager.getTransaction(any()))
                .willReturn(transactionStatus);

        willDoNothing()
                .given(eventService).persistLog(methodResult);

        eventfulMethodInterceptor.invoke(methodInvocation);

        verify(eventService).persistLog(methodResult);
    }

    @Test
    public void invoke_eventfulMethodThrowsException_transactionRolledBack() throws Throwable {
        MethodInvocation methodInvocation = mock(MethodInvocation.class);
        Method eventfulMethod = TestEventfulService.class.getMethod("eventfulMethod");

        given(methodInvocation.getMethod())
                .willReturn(eventfulMethod);
        given(methodInvocation.proceed())
                .willThrow(new RuntimeException("You're failing Seymour!"));

        TransactionStatus transactionStatus = mock(TransactionStatus.class);
        given(transactionManager.getTransaction(any()))
                .willReturn(transactionStatus);

        assertThatThrownBy(() -> eventfulMethodInterceptor.invoke(methodInvocation))
                .isNotNull();

        verify(transactionManager).rollback(transactionStatus);
        verify(transactionManager, never()).commit(transactionStatus);
    }

    @Test
    public void invoke_eventfulMethodThrowsException_throwsSameException() throws Throwable {
        MethodInvocation methodInvocation = mock(MethodInvocation.class);
        Method eventfulMethod = TestEventfulService.class.getMethod("eventfulMethod");
        RuntimeException exception = new RuntimeException("You're failing Seymour!");

        given(methodInvocation.getMethod())
                .willReturn(eventfulMethod);
        given(methodInvocation.proceed())
                .willThrow(exception);

        TransactionStatus transactionStatus = mock(TransactionStatus.class);
        given(transactionManager.getTransaction(any()))
                .willReturn(transactionStatus);

        assertThatThrownBy(() -> eventfulMethodInterceptor.invoke(methodInvocation))
                .isSameAs(exception);
    }

    @Test
    public void invoke_eventfulMethodThrowsException_doesNotPersistLog() throws Throwable {
        MethodInvocation methodInvocation = mock(MethodInvocation.class);
        Method eventfulMethod = TestEventfulService.class.getMethod("eventfulMethod");
        TransactionStatus transactionStatus = mock(TransactionStatus.class);

        given(methodInvocation.getMethod())
                .willReturn(eventfulMethod);
        given(methodInvocation.proceed())
                .willThrow(new RuntimeException("You're failing Seymour!"));

        given(transactionManager.getTransaction(any()))
                .willReturn(transactionStatus);

        assertThatThrownBy(() -> eventfulMethodInterceptor.invoke(methodInvocation))
                .isNotNull();

        verify(eventService, never()).persistLog(anyObject());
    }

    @EventfulService
    private class TestEventfulService {

        @Eventful
        public EventPayload eventfulMethod() {
            return eventPayload;
        }

        public String nonEventfulMethod() {
            return "I don't need to be monitored";
        }
    }
}