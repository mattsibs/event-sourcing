package com.event.sourcing;

import com.event.sourcing.model.permission.Permission;
import com.event.sourcing.model.user.User;
import com.event.sourcing.service.permission.PermissionService;
import com.event.sourcing.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void startUpService_onContextLoad_createsPermissions() {
        List<Permission> permissions = permissionService.findPermissions();

        for (Permission.Action action : Permission.Action.values()) {
            List<Permission> resourcesForAction = permissions.stream()
                    .filter(permission -> permission.getAction().equals(action))
                    .collect(toList());

            assertThat(resourcesForAction)
                    .extracting(Permission::getResource)
                    .containsOnlyOnce(Permission.Resource.values());
        }
    }

    @Test
    public void startUp_fileLog_createsUsers() {
        List<User> users = userService.getUsers();
        assertThat(users).hasSize(1);
    }

}