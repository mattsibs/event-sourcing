package com.event.sourcing.config;

import com.event.sourcing.proxy.EventfulMethodInterceptor;
import com.event.sourcing.service.UserService;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SpringServiceConfiguration {


    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public EventfulMethodInterceptor eventfulMethodInterceptor() {
        return new EventfulMethodInterceptor();
    }

    @Bean
    @Primary
    public ProxyFactoryBean testProxyFactoryBean() {
        ProxyFactoryBean testProxyFactoryBean = new ProxyFactoryBean();
        testProxyFactoryBean.setTarget(userService());
        testProxyFactoryBean.setInterceptorNames("eventfulMethodInterceptor");
        return testProxyFactoryBean;
    }
}
