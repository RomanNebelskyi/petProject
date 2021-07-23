package com.example.petProject.config.security;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class PasswordConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    passwordEncoder.encode(randomSalt());
    //return new Pbkdf2PasswordEncoder(randomSalt());
    //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    return new BCryptPasswordEncoder(8);
  }

  private String randomSalt() {

    String sourceForSalt = "abcdefghijklnmopqrstuvwzyx1234567890";
    StringBuilder salt = new StringBuilder();
    for (int i = 0; i < ThreadLocalRandom.current().nextInt(3, sourceForSalt.length() + 1); ++i) {
      salt.append(
          sourceForSalt.charAt(ThreadLocalRandom.current().nextInt(0, sourceForSalt.length())));
    }
    return salt.toString();
  }

}
