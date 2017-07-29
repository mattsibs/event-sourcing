package com.event.sourcing.proxy;


import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.annotation.EventfulService;
import com.event.sourcing.event.EventPayload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.TargetSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EventfulBeanPostProcessorTest {

    @Mock
    private EventfulMethodInterceptor eventfulMethodInterceptor;

    private EventfulBeanPostProcessor eventfulBeanPostProcessor;

    @Before
    public void setUp() throws Exception {
        eventfulBeanPostProcessor = new EventfulBeanPostProcessor(eventfulMethodInterceptor);
    }

    @Test
    public void getAdvicesAndAdvisorsForBean_classNotAnnotated_doesNotProxy() throws Exception {
        Object[] proxies = eventfulBeanPostProcessor
                .getAdvicesAndAdvisorsForBean(TestUnannotatedService.class, "TestUnannotatedService", mock(TargetSource.class));

        assertThat(proxies).isNull();
    }

    @Test
    public void getAdvicesAndAdvisorsForBean_classAnnotatedAndOneEventfulMethod_returnsProxy() throws Exception {
        Object[] proxies = eventfulBeanPostProcessor
                .getAdvicesAndAdvisorsForBean(TestEventfulService.class, "TestEventfulService", mock(TargetSource.class));

        assertThat(proxies)
                .containsOnly(eventfulMethodInterceptor);
    }

    @Test
    public void getAdvicesAndAdvisorsForBean_classAnnotatedEventfulMethodHasWrongReturnType_throwsIllegalStateException() throws Exception {
        assertThatThrownBy(() ->

                eventfulBeanPostProcessor
                        .getAdvicesAndAdvisorsForBean(TestUnproxyableEventfulService.class, "TestUnproxyableEventfulService", mock(TargetSource.class)))

                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Method testMethod returns type class java.lang.String, Eventful annotation must return EventPayload");
    }

    @EventfulService
    private static class TestEventfulService {

        @Eventful
        public EventPayload testMethod() {
            return mock(EventPayload.class);
        }

    }

    @EventfulService
    private static class TestUnproxyableEventfulService {

        @Eventful
        public String testMethod() {
            return "thisIsOfTheWrongType";
        }

    }

    private static class TestUnannotatedService {

    }

}