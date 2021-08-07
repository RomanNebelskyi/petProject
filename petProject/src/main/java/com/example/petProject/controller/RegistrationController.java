package com.example.petProject.controller;


import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.repo.UserRepo;
import com.example.petProject.service.MailService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserRepo userRepo;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepo repo, MailService mailService,
            PasswordEncoder passwordEncoder) {
        this.userRepo = repo;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping
    public String registerPage() {
        return "registration";
    }

    @GetMapping("/confirm/{uid}")
    public String confirmAccount(Model model, @PathVariable String uid) {
        try {
            User user = userRepo.getByConfirmationCode(uid)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "UUID account was not found( uuid=" + uid));

            boolean isTimeExpired = user
                    .getRegistrationDate()
                    .plus(30, ChronoUnit.MINUTES)
                    .compareTo(LocalDateTime.now())
                    < 0;

            if (isTimeExpired) {
                model.addAttribute("error_message",
                        "Sorry, but the time of your activation has expired. We sent you new code, so activate your account. ");

                user.setConfirmationCode(generateCode());
                mailService.sendConfirmationCode(user.getConfirmationCode(), user.getEmail());
                return "registration";
            } else {
                user.setConfirmationCode("");
                userRepo.save(user);
                return "redirect:/login";
            }
        } catch (UsernameNotFoundException e) {
            model.addAttribute("error_message", "Sorry, but user can*t be found");
            e.printStackTrace();
        }
        return "redirect:/login";
    }

    @PostMapping
    public String register(Model model, String name, String email, String password,
            String confirm) {

        if (!password.equals(confirm)) {
            model.addAttribute("error_message", "Sorry, your passwords must be equal.");
            return "registration";
        } else if (userRepo.getByEmail(email).isPresent()) {
            model.addAttribute("error_message",
                    "Account with mail +" + email + " is already registered. Please Log in.");
            return "registration";
        } else {
            name = name.trim();
            User user = new User();
            user.setEmail(email);
            user.setConfirmationCode(generateCode());
            user.setName(name);
            user.setPass(passwordEncoder.encode(password));
            user.setRole(Role.USER);
            user.setRegistrationDate(LocalDateTime.now());
            user.setConfirmationCode("");
            userRepo.save(user);

            mailService.sendConfirmationCode(user.getConfirmationCode(), user.getEmail());
        }

        return "redirect:/login";
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

}
