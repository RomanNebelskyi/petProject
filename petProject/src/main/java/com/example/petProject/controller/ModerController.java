package com.example.petProject.controller;


import com.example.petProject.model.Task;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
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
@PreAuthorize("hasAuthority('MODERATOR')")
public class ModerController {

  private UserRepo userRepo;
  private TaskRepo taskRepo;

  public ModerController(UserRepo userRepo, TaskRepo taskRepo) {
    this.userRepo = userRepo;
    this.taskRepo = taskRepo;
  }

  private static Comparator<Task> getComparator(String toSort, String flow) {
    Comparator<Task> comparator = null;

    switch (toSort) {
      case "id": {
        comparator = Comparator.comparing(Task::getId);
        break;
      }
      case "name": {
        comparator = Comparator.comparing(Task::getName);
        break;
      }
      case "buyer": {
        comparator = Comparator.comparing(e -> e.getBuyer().getName());
        break;
      }
      case "status": {
        comparator = Comparator.comparing(e -> e.getStatus().toString());
        break;
      }
      case "deadline": {
        comparator = Comparator.comparing(Task::getDeadline);
        break;
      }
      case "price": {
        comparator = Comparator.comparing(Task::getPrice);
        break;
      }
      case "description": {
        comparator = Comparator.comparing(Task::getDescription);
        break;
      }
      case "workers": {
        comparator = Comparator.comparing(a -> a.getWorkers().size());
        break;
      }
      default:
        comparator = Comparator.comparing(Task::getId);
        break;
    }

    if (flow.equals("DESC")) {
      comparator = comparator.reversed();
    }
    return comparator;

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
      @RequestParam("toSort") String toSort,
      @RequestParam("flow") String flow) {

    List<Task> tasks = taskRepo.findAll();
    tasks.sort(getComparator(toSort, flow));
    model.addAttribute("tasks", tasks);
    model.addAttribute("flow", flow);
    model.addAttribute("toSort", toSort);

    return "moder";
  }


}
