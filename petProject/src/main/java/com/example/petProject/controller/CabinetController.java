package com.example.petProject.controller;

import com.example.petProject.Dto.TaskDto;
import com.example.petProject.model.Task;
import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {

  @Autowired
  private TaskRepo taskRepo;
  @Autowired
  private UserRepo userRepo;

  @GetMapping
  public String mainPage(Model model) {

    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    List<Task> test = taskRepo.getAllByBuyerId(user.getId());
    List<TaskDto> tasks = taskRepo.findAllByBuyer(user).stream()
        .map(Task::toDto)
        .collect(Collectors.toList());

    model.addAttribute("tasks", tasks);
    if (user.getRole() == Role.USER) {
      return "cabinetUser";
    } else if (user.getRole() == Role.MODERATOR) {
      return "redirect:/moder";
    } else {
      return "cabinetProgrammer";
    }
  }

}
