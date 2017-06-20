package com.event.sourcing.model;

import com.event.sourcing.model.user.User;
import com.google.common.base.MoreObjects;

public class CreateUserEvent {

    private final User user;

    private CreateUserEvent(final User user) {
        this.user = user;
    }

    public static CreateUserEvent of(final User user) {
        return new CreateUserEvent(user);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("user", user)
                .toString();
    }
}
