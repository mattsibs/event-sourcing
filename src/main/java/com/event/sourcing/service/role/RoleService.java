package com.event.sourcing.service.role;

import com.event.sourcing.annotation.Eventful;
import com.event.sourcing.annotation.EventfulService;
import com.event.sourcing.event.role.CreateRoleEvent;
import com.event.sourcing.event.role.GrantRoleEvent;
import com.event.sourcing.model.DataManager;
import com.event.sourcing.model.permission.Permission;
import com.event.sourcing.model.role.Role;
import com.event.sourcing.model.role.RoleRepository;
import com.event.sourcing.model.user.User;
import com.event.sourcing.model.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;

@EventfulService
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(final DataManager dataManager) {
        this.roleRepository = dataManager.getRoleRepository();
        this.userRepository = dataManager.getUserRepository();
    }

    public Optional<Role> find(final long id) {
        return Optional.ofNullable(roleRepository.findOne(id));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Eventful
    public CreateRoleEvent create(final String roleName, final List<Permission> permissions) {
        Role role = new Role();
        role.setLogId(randomUUID().toString());
        role.setName(roleName);
        role.setPermissions(permissions);
        Role createdRole = roleRepository.save(role);
        return new CreateRoleEvent(createdRole);
    }

    @Eventful
    public GrantRoleEvent grantRole(final User user, final Role role) {
        user.getRoles().add(role);
        User updatedUser = userRepository.save(user);
        return new GrantRoleEvent(updatedUser, role);
    }

}
