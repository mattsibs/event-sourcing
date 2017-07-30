package com.event.sourcing.service;

import com.event.sourcing.model.permission.Permission;
import com.event.sourcing.service.event.EventService;
import com.event.sourcing.service.permission.PermissionService;

public class StartUpService {

    private final EventService eventService;
    private final PermissionService permissionService;

    public StartUpService(final EventService eventService, final PermissionService permissionService) {
        this.eventService = eventService;
        this.permissionService = permissionService;
    }

    public void startUp() {
        createPermissions();
        eventService.updateEvents();
    }

    private void createPermissions() {
        for (Permission.Action action : Permission.Action.values()) {
            for (Permission.Resource resource : Permission.Resource.values()) {
                permissionService.create(action, resource);
            }
        }
    }
}
