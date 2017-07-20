package com.event.sourcing.event.role;

import com.event.sourcing.event.EventPayload;
import com.event.sourcing.model.DataManager;
import com.event.sourcing.model.role.Role;
import com.event.sourcing.model.role.RoleRepository;
import com.event.sourcing.model.user.User;
import com.event.sourcing.model.user.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.transaction.Transactional;

public class GrantRoleEvent extends EventPayload<Role> {

    @JsonProperty
    private String userLogId;

    @JsonProperty
    private String roleLogId;

    private GrantRoleEvent() {
        super(Role.class, Type.UPDATE);
    }

    public GrantRoleEvent(final User user, final Role role) {
        super(Role.class, Type.UPDATE);
        this.userLogId = user.getLogId();
        this.roleLogId = role.getLogId();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Role process(final DataManager dataManager) {
        UserRepository userRepository = dataManager.getUserRepository();
        RoleRepository roleRepository = dataManager.getRoleRepository();

        Role role = roleRepository.findByLogId(roleLogId);
        User user = userRepository.findByLogId(userLogId);

        user.getRoles().add(role);
        userRepository.save(user);
        return role;
    }



}
