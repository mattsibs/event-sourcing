package com.event.sourcing.controller;

import com.event.sourcing.model.user.User;
import com.event.sourcing.service.user.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public void create() {
        userService.createUser(new CreateUserRequest("mattSibson", "Matthew", "Sibson", "itsASecret"));
    }

    public static class CreateUserRequest {

        @JsonProperty private final String username;
        @JsonProperty private final String firstName;
        @JsonProperty private final String lastName;
        @JsonProperty private final String password;

        public CreateUserRequest(final String username, final String firstName, final String lastName, final String password) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getPassword() {
            return password;
        }
    }

}
