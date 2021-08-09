package com.example.petProject.model.enums;

import java.util.Arrays;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, MODERATOR, PROGRAMMER, ADMIN;

    public static String[] adminRoles() {
        return Arrays.stream(Role.values())
                .filter(e -> e != Role.ADMIN)
                .map(Role::name)
                .toArray(String[]::new);
    }

    @Override
    public String getAuthority() {
        return this.name();
    }

}
