package com.example.petProject.controller;

import com.example.petProject.Dto.TaskDto;
import com.example.petProject.model.Task;
import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import com.example.petProject.service.ComparatorService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final ComparatorService comparatorService;

    public CabinetController(TaskRepo taskRepo, UserRepo userRepo,
            ComparatorService comparatorService) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.comparatorService = comparatorService;
    }

    @GetMapping()
    public String mainPage(Model model,
            @RequestParam(value = "error_message", required = false) String message,
            @RequestParam(value = "toSort", required = false) String toSort,
            @RequestParam(value = "flow", required = false) String flow) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getRole() == Role.ADMIN) {
            return "redirect:/ admin";
        }
        if (user.getRole() == Role.PROGRAMMER) {
            model.addAttribute("task", user.getCurrentTask());
            return "cabinetProgrammer";
        }

        if (toSort == null) {
            toSort = "name";
        }
        if (flow == null) {
            flow = "ASC";
        }

        List<TaskDto> tasks = taskRepo.findAllByBuyer(user).stream()
                .map(Task::toDto)
                .sorted(comparatorService.getDtoComparator(toSort, flow))
                .collect(Collectors.toList());

        model.addAttribute("tasks", tasks);
        model.addAttribute("error_message", message);
        model.addAttribute("toSort", toSort);
        model.addAttribute("flow", flow);
        if (user.getRole() == Role.USER) {
            return "cabinetUser";
        } else {
            return "redirect:/moder";
        }
    }

}
