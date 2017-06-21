package com.event.sourcing;

import com.event.sourcing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class Application {

    @Autowired
    private UserService userService;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
