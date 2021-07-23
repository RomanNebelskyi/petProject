package com.example.petProject.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  USER, MODERATOR, PROGRAMMER, ADMIN, MAIN_ADMIN, BANNED;

  @Override
  public String getAuthority() {
    return this.name();
  }
}
