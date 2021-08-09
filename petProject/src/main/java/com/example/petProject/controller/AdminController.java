package com.example.petProject.controller;

import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.repo.UserRepo;
import com.example.petProject.service.ComparatorService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final UserRepo userRepo;


    public AdminController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String page(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("flow", "ASC");
        model.addAttribute("toSort", "id");
        return "admin";
    }

    @PostMapping
    public String sorting(Model model,
            @RequestParam(value = "toSort") String toSort,
            @RequestParam(value = "flow") String flow) {

        List<User> users = userRepo.findAll();
        users.sort(ComparatorService.getUserComparator(toSort, flow));
        model.addAttribute("users", users);
        model.addAttribute("flow", flow);
        model.addAttribute("toSort", toSort);

        return "admin";
    }

    @GetMapping("/user-details")
    public String userDetails(@AuthenticationPrincipal User authUser, Model model,
            @RequestParam("userId") long userId) {
        Optional<User> checkUser = userRepo.findById(userId);
        if (!checkUser.isPresent()) {
            model.addAttribute("error_message", "User not found");
            return "admin";
        }

        User foundUser = checkUser.get();
        model.addAttribute("usr", foundUser);

        boolean isEditable = authUser.getCurrentTask() == null && (authUser.getTasks() == null
                || authUser.getTasks().size() == 0);
        model.addAttribute("isEditable", isEditable);
        model.addAttribute("roles", Role.values()); // all roles

        if (foundUser.getRole() == Role.USER) {
            model.addAttribute("tasks", foundUser.getTasks());
        } else if (foundUser.getRole() == Role.PROGRAMMER) {
            model.addAttribute("currentTask", foundUser.getCurrentTask());
        }

        return "userDetails";
    }


}
