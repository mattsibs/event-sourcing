package com.event.sourcing.controller;

import com.event.sourcing.model.permission.Permission;
import com.event.sourcing.model.role.Role;
import com.event.sourcing.model.user.User;
import com.event.sourcing.service.permission.PermissionService;
import com.event.sourcing.service.role.RoleService;
import com.event.sourcing.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;
    private final PermissionService permissionService;

    public RoleController(final RoleService roleService, final UserService userService,
                          final PermissionService permissionService) {
        this.roleService = roleService;
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @GetMapping(value = "/role")
    @ResponseBody
    public List<Role> findRoles() {
        return roleService.findAll();
    }

    @GetMapping(value = "/permission")
    @ResponseBody
    public List<Permission> findPermissions() {
        return permissionService.findPermissions();
    }

    @PostMapping(value = "/role")
    public void create() {
        Permission permission = new Permission();
        permission.setAction(Permission.Action.CREATE);
        permission.setResource(Permission.Resource.USER);
        roleService.create("role1", permissionService.findPermissions());
    }

    @PutMapping(value = "role/{roleId}")
    public void grantRole(@PathVariable final long roleId, @RequestParam final long userId) {
        User user = userService.find(userId).orElseThrow(() -> new RuntimeException("not-found"));
        Role role = roleService.find(roleId).orElseThrow(() -> new RuntimeException("not-found"));
        roleService.grantRole(user, role);
    }

}
