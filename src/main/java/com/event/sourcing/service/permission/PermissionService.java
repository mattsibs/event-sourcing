package com.event.sourcing.service.permission;

import com.event.sourcing.model.DataManager;
import com.event.sourcing.model.permission.Permission;
import com.event.sourcing.model.permission.PermissionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(final DataManager dataManager) {
        this.permissionRepository = dataManager.getPermissionRepository();
    }

    public List<Permission> findPermissions() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> find(final Permission.Action action, final Permission.Resource resource) {
        return Optional.ofNullable(permissionRepository.findByActionAndResource(action, resource));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Permission create(final Permission.Action action, final Permission.Resource resource) {
        Permission permission = new Permission();
        permission.setAction(action);
        permission.setResource(resource);
        return permissionRepository.save(permission);
    }
}
