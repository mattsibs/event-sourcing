package com.event.sourcing;

import com.event.sourcing.model.permission.Permission;
import com.event.sourcing.model.user.User;
import com.event.sourcing.service.permission.PermissionService;
import com.event.sourcing.service.role.RoleService;
import com.event.sourcing.service.user.UserService;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ApplicationTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @ActiveProfiles("emptyFile")
    public static class EmptyLogFileTest {

        @Autowired
        private UserService userService;

        @Autowired
        private RoleService roleService;

        @Autowired
        private PermissionService permissionService;

        @Test
        public void run_fileLogEmpty_createsNoUsers() {
            assertThat(userService.getUsers())
                    .isEmpty();
        }

        @Test
        public void run_fileLogEmpty_createsNoRoles() {
            assertThat(roleService.findAll())
                    .isEmpty();
        }

        @Test
        public void run_fileLogEmpty_createsBasicPermissions() {
            List<Permission> permissions = permissionService.findPermissions();

            for (Permission.Resource resource : Permission.Resource.values()) {
                assertThat(permissions.stream().filter(permission -> permission.getResource().equals(resource)))
                        .extracting(Permission::getAction)
                        .containsOnly(Permission.Action.values());
            }

        }
    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @ActiveProfiles("logFilePresent")
    public static class PopulatedLogFileTest {

        @Autowired
        private UserService userService;

        @Test
        public void run_fileLogContainsTwoUsers_createsTwoUsers() {
            List<User> users = userService.getUsers();

            assertThat(users)
                    .extracting(User::getLogId)
                    .containsOnly(
                            "8a7ad598-7fcd-4e49-b181-66ea6f64035c",
                            "4f1bb4b3-9b83-4641-b1f0-4bdca88ef3e8"
                    );
        }
    }
}