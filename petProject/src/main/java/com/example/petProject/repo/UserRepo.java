package com.example.petProject.repo;


import com.example.petProject.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
public Optional<User> getByEmail(String email);
public Optional<User> getByConfirmationCode(String code);
}
