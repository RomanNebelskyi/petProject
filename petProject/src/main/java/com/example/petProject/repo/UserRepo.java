package com.example.petProject.repo;

import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

  public Optional<User> getByEmail(String email);

  public Optional<User> getByConfirmationCode(String code);

  public List<User> findByRoleAndCurrentTaskNull(Role role);
}
