package com.example.petProject.controller;

import com.example.petProject.model.User;
import com.example.petProject.repo.UserRepo;
import com.example.petProject.service.MailService;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {


  private final UserRepo userRepo;
  private final MailService mailService;
  private final PasswordEncoder encoder;

  public ForgotPasswordController(UserRepo userRepo, MailService mailService,
      PasswordEncoder encoder) {
    this.userRepo = userRepo;
    this.mailService = mailService;
    this.encoder = encoder;
  }

  @GetMapping
  public String page(Model model,
      @RequestParam(value = "error_message", required = false) String message) {
    if (message != null && message.length() != 0) {
      model.addAttribute("error_message", message);
    }
    return "forgotPassword";
  }

  @PostMapping
  public String sendNewPassword(Model model,
      @RequestParam(value = "email", required = false) String email) {

    Optional<User> isPresent = userRepo.getByEmail(email);
    if (!isPresent.isPresent()) {
      model.addAttribute("error_message", "Sorry, but your account can*t be found");
      return "redirect:/forgot-password";
    } else {

      User currentUser = isPresent.get();

      StringBuilder code = new StringBuilder();
      String source = "abcdefghijklnmopqrstuvwzyx1234567890";
      for (int i = 0; i < 5; ++i) {
        code.append(source.charAt(ThreadLocalRandom.current().nextInt(0, source.length())));
      }
      mailService.sendNewPass(currentUser.getEmail(), code.toString());
      currentUser.setPass(encoder.encode(code.toString()));
      userRepo.save(currentUser);

      return "redirect:/login";
    }

  }

}
