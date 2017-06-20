package com.event.sourcing.service;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.annotation.EventfulService;
import com.event.sourcing.model.CreateUserEvent;
import com.event.sourcing.model.user.User;
import com.event.sourcing.model.user.UserRepository;

import java.util.List;

@EventfulService
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Eventful
    public String testMethod() {
        return "Hello";
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Eventful
    public CreateUserEvent createUser(final CreateUserEvent createUserEvent) {
        User user = new User();
        user.setUsername("name");
        user.setPassword("name");
        user.setLastName("name");
        user.setFirstName("name");
        User save = userRepository.save(user);
        return CreateUserEvent.of(save);
    }

}
