package com.event.sourcing.service.user;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.annotation.EventfulService;
import com.event.sourcing.controller.UserController;
import com.event.sourcing.event.user.CreateUserEvent;
import com.event.sourcing.model.DataManager;
import com.event.sourcing.model.user.User;
import com.event.sourcing.model.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;

@EventfulService
public class UserService {

    private final UserRepository userRepository;

    public UserService(final DataManager dataManager) {
        this.userRepository = dataManager.getUserRepository();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> find(final long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Eventful
    public CreateUserEvent createUser(final UserController.CreateUserRequest createUserRequest) {
        User user = new User();
        user.setLogId(randomUUID().toString());
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(createUserRequest.getPassword());
        user.setLastName(createUserRequest.getLastName());
        user.setFirstName(createUserRequest.getFirstName());
        User save = userRepository.save(user);
        return CreateUserEvent.of(save);
    }


}
