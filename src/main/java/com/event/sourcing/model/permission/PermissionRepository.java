package com.event.sourcing.model.permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByActionAndResource(final Permission.Action action, final Permission.Resource resource);
}
