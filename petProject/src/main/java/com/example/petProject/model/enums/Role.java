package com.example.petProject.model.enums;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  USER, MODERATOR, PROGRAMMER, ADMIN, MAIN_ADMIN, BANNED;

  public static String[] adminRoles() {
    return Arrays.stream(Role.values())
        .filter(e -> e != Role.ADMIN && e != Role.MAIN_ADMIN && e != Role.BANNED)
        .map(Role::name)
        .toArray(String[]::new);
  }

  @Override
  public String getAuthority() {
    return this.name();
  }
}
