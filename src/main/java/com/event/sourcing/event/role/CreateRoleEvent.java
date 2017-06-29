package com.event.sourcing.event.role;

import com.event.sourcing.event.EventPayload;
import com.event.sourcing.model.DataManager;
import com.event.sourcing.model.permission.Permission;
import com.event.sourcing.model.permission.PermissionRepository;
import com.event.sourcing.model.role.Role;
import com.event.sourcing.model.role.RoleRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

public class CreateRoleEvent extends EventPayload<Role> {

    @JsonProperty
    private String logId;

    @JsonProperty
    private String name;

    @JsonProperty
    private List<Permission> permissionList = new ArrayList<>();

    private CreateRoleEvent() {
        super(Role.class, Type.CREATE);
    }

    public CreateRoleEvent(final Role role) {
        super(Role.class, Type.CREATE);
        this.logId = role.getLogId();
        this.name = role.getName();
        this.permissionList.addAll(role.getPermissions());
    }

    @Override
    public Role process(final DataManager dataManager) {
        RoleRepository roleRepository = dataManager.getRoleRepository();
        PermissionRepository permissionRepository = dataManager.getPermissionRepository();

        Role role = new Role();
        role.setLogId(logId);
        role.setName(name);

        List<Permission> permissions = permissionList.stream()
                .map(permission -> permissionRepository
                        .findByActionAndResource(permission.getAction(), permission.getResource()))
                .filter(Objects::nonNull)
                .collect(toList());

        role.setPermissions(permissions);
        return roleRepository.save(role);
    }
}
