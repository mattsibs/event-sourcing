package com.event.sourcing.config;

import com.event.sourcing.model.DataManager;
import com.event.sourcing.model.permission.PermissionRepository;
import com.event.sourcing.model.role.RoleRepository;
import com.event.sourcing.model.user.UserRepository;
import com.event.sourcing.proxy.EventfulBeanPostProcessor;
import com.event.sourcing.proxy.EventfulMethodInterceptor;
import com.event.sourcing.service.StartUpService;
import com.event.sourcing.service.event.EventService;
import com.event.sourcing.service.event.EventServiceFactory;
import com.event.sourcing.service.permission.PermissionService;
import com.event.sourcing.service.role.RoleService;
import com.event.sourcing.service.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringServiceConfiguration {

    private static final Logger L = Logger.getLogger(SpringServiceConfiguration.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ServerConfiguration serverConfiguration;

    @Bean
    public EventService eventService() {
        return EventServiceFactory.createFileBasedEventService(serverConfiguration.getLogFile(), event -> {
            L.info(event.getEventPayload().process(dataManager()));
        });
    }

    @Bean
    public EventfulMethodInterceptor eventfulMethodInterceptor() {
        return new EventfulMethodInterceptor(eventService(), platformTransactionManager);
    }

    @Bean
    public DataManager dataManager() {
        return new DataManager(userRepository, roleRepository, permissionRepository);
    }

    @Bean
    public UserService userService() {
        return new UserService(dataManager());
    }

    @Bean
    public RoleService roleService() {
        return new RoleService(dataManager());
    }

    @Bean
    public PermissionService permissionService() {
        return new PermissionService(dataManager());
    }

    @Bean
    public StartUpService startUpService() {
        return new StartUpService(eventService(), permissionService());
    }

    @Bean
    public EventfulBeanPostProcessor beanPostProcessor() {
        return new EventfulBeanPostProcessor(eventfulMethodInterceptor());
    }
}
