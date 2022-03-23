package com.bancocaminos.impactbackendapi.user.rest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class RoleTest {

    @Test
    public void roleEntity() {
        Role role = Role.ADMIN;
        Role roleUser = Role.USER;

        assertEquals(Role.ADMIN.name(), role.name());
        assertEquals(Role.USER.name(), roleUser.name());
    }
}
