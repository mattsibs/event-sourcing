package com.event.sourcing.model;

import com.event.sourcing.model.permission.PermissionRepository;
import com.event.sourcing.model.role.RoleRepository;
import com.event.sourcing.model.user.UserRepository;

public class DataManager {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public DataManager(final UserRepository userRepository, final RoleRepository roleRepository,
                       final PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public PermissionRepository getPermissionRepository() {
        return permissionRepository;
    }
}
