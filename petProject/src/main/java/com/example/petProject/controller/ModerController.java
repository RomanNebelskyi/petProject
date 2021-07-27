package com.example.petProject.controller;


import com.example.petProject.model.Task;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import com.example.petProject.service.ComparatorService;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/moder")
@PreAuthorize("hasAnyAuthority('MODERATOR','ADMIN','MAIN_ADMIN')")
public class ModerController {

  private final UserRepo userRepo;
  private final TaskRepo taskRepo;


  public ModerController(UserRepo userRepo, TaskRepo taskRepo,
      ComparatorService comparatorService) {
    this.userRepo = userRepo;
    this.taskRepo = taskRepo;
  }


  @GetMapping
  public String page(Model model) {

    List<Task> tasks = taskRepo.findAll();
    model.addAttribute("tasks", tasks);
    model.addAttribute("flow", "ASC");
    model.addAttribute("toSort", "id");
    return "moder";

  }

  @PostMapping
  public String sorting(Model model,
      @RequestParam(value = "toSort") String toSort,
      @RequestParam(value = "flow") String flow) {

    List<Task> tasks = taskRepo.findAll();
    tasks.sort(ComparatorService.getTaskComparator(toSort, flow));
    model.addAttribute("tasks", tasks);
    model.addAttribute("flow", flow);
    model.addAttribute("toSort", toSort);

    return "moder";
  }


}
