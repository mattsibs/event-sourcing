package com.event.sourcing.config;

import com.event.sourcing.event.EventLogger;
import com.event.sourcing.event.EventService;
import com.event.sourcing.model.user.UserRepository;
import com.event.sourcing.proxy.EventfulBeanPostProcessor;
import com.event.sourcing.proxy.EventfulMethodInterceptor;
import com.event.sourcing.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringServiceConfiguration {

    private static final Logger L = Logger.getLogger(SpringServiceConfiguration.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public EventLogger eventLogger() {
        return object -> {

//            throw new RuntimeException("Error logging");

        };
    }

    @Bean
    public EventService eventService() {
        return new EventService(eventLogger());
    }

    @Bean
    public EventfulMethodInterceptor eventfulMethodInterceptor() {
        return new EventfulMethodInterceptor(eventService(), platformTransactionManager);
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository);
    }

    @Bean
    public EventfulBeanPostProcessor beanPostProcessor() {
        return new EventfulBeanPostProcessor(eventfulMethodInterceptor());
    }
}
