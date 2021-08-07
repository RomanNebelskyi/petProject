package com.example.petProject.controller;

import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.repo.UserRepo;
import javax.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/edit-acc")
public class EditAccController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public EditAccController(UserRepo userRepo,
            PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String page(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("usr", user);
        return "account";
    }

    @PostMapping("/name")
    public String changeName(Model model,
            @AuthenticationPrincipal User user,
            @NotNull @RequestParam("newEmail") String email,
            @NotNull @RequestParam("newName") String name) {

        user.setName(name);
        user.setEmail(email);
        userRepo.save(user);
        model.addAttribute("usr", user);
        return "account";
    }


    @PostMapping("/password")
    public String changePass(Model model,
            @AuthenticationPrincipal User user,
            @NotNull @RequestParam("old") String oldPassword,
            @NotNull @RequestParam("password") String newPass, @NotNull @RequestParam("confirm")
            String confirm) {

        if (!newPass.equals(confirm)) {
            model.addAttribute("error_message", "Sorry, but passwords are not equal.");
            return "passChange";
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("error_message", "Sorry, old password is wrong.");
            return "passChange";
        }
        user.setPass(passwordEncoder.encode(newPass));
        userRepo.save(user);
        return "passChange";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/role")
    public String changeRole(
            @RequestParam("userId") long userId,
            @RequestParam("role") String role) {

        User currentUser = userRepo.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User with id" + userId + " can*t be found"));
        currentUser.setRole(Role.valueOf(role));
        userRepo.save(currentUser);
        return "redirect:/admin/user-details?userId=" + userId;
    }
}
