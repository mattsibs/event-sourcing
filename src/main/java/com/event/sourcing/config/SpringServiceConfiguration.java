package com.event.sourcing.config;

import com.event.sourcing.event.EventLogger;
import com.event.sourcing.event.EventService;
import com.event.sourcing.proxy.EventfulBeanPostProcessor;
import com.event.sourcing.proxy.EventfulMethodInterceptor;
import com.event.sourcing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringServiceConfiguration {

    @Bean
    public EventLogger eventLogger() {
        return System.out::println;
    }

    @Bean
    public EventService eventService() {
        return new EventService(eventLogger());
    }

    @Bean
    public EventfulMethodInterceptor eventfulMethodInterceptor() {
        return new EventfulMethodInterceptor(eventService());
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public EventfulBeanPostProcessor beanPostProcessor() {
        return new EventfulBeanPostProcessor(eventfulMethodInterceptor());
    }
}
