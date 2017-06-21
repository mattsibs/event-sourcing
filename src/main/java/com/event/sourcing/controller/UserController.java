package com.event.sourcing.controller;

import com.event.sourcing.model.CreateUserEvent;
import com.event.sourcing.model.user.User;
import com.event.sourcing.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    @ResponseBody
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(value = "/users")
    public void create(final String userName) {
        userService.createUser(CreateUserEvent.of(null));
    }

}
