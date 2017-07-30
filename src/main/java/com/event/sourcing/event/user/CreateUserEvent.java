package com.event.sourcing.event.user;

import com.event.sourcing.event.EventPayload;
import com.event.sourcing.model.DataManager;
import com.event.sourcing.model.user.User;
import com.event.sourcing.model.user.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserEvent extends EventPayload<User> {

    @JsonProperty
    private String logId;
    @JsonProperty
    private String username;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String password;

    private User user;

    private CreateUserEvent() {
        super(User.class, Type.CREATE);
    }

    @Override
    public User process(final DataManager dataManager) {
        UserRepository userRepository = dataManager.getUserRepository();
        User user = new User();
        user.setLogId(logId);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        User save = userRepository.save(user);
        return save;
    }


    private CreateUserEvent(final User user) {
        super(User.class, Type.CREATE);
        this.user = user;
        this.logId = user.getLogId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
    }

    public static CreateUserEvent of(final User user) {
        return new CreateUserEvent(user);
    }

}
