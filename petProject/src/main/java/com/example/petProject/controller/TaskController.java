package com.example.petProject.controller;

import com.example.petProject.model.Task;
import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.model.enums.Status;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import java.util.Date;
import java.util.NoSuchElementException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/task")
public class TaskController {


  private final TaskRepo taskRepo;
  private final UserRepo userRepo;

  public TaskController(TaskRepo taskRepo, UserRepo userRepo) {
    this.taskRepo = taskRepo;
    this.userRepo = userRepo;
  }


  @GetMapping("/add")
  public String page() {
    return "add-task";
  }

  @GetMapping("/cancel")
  @PreAuthorize("hasAnyAuthority({'USER', 'MODERATOR'})")
  public String cancelTask(@RequestParam("id") long taskId) {

    Task task = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("TaskDto with id " + taskId + " can*t be found!"));
    task.setStatus(Status.CANCELLED);
    task.setWorkers(null);
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    task.setBuyer(user);
    taskRepo.save(task);

    return "redirect:/cabinet";
  }

  @PostMapping("/add")
  @PreAuthorize("hasAnyAuthority({'USER', 'MODERATOR'})")
  public String addTask(
      @RequestParam("name") String name,
      @RequestParam("deadline")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deadline,
      @RequestParam("price") int price,
      @RequestParam("description") String description) {

    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    System.out.println(deadline);
    try {
      if (user.getRole() != Role.USER && user.getRole() != Role.MODERATOR) {
        throw new IllegalAccessException("Only User or Moderator can add tasks!" + user.toString());
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    Task task = new Task();
    task.setBuyer(user);
    task.setPrice(price);
    task.setDescription(description);
    task.setStatus(Status.ESTIMATED);
    task.setDeadline(deadline);
    task.setName(name);

    taskRepo.save(task);
    return "redirect:/cabinet";
  }


  @GetMapping("/details?id={id}")
  @PreAuthorize("hasAuthority('MODERATOR')")
  public String details(Model model, @PathVariable long id) {

    Task task = taskRepo.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Task with Id = " + id + " can*t be found!"));
    model.addAttribute("task", task);
    model.addAttribute("workers", task.getWorkers());
    //////////////////////////changed add-task to addTask!!!!!!!!!!;

    return "taskDetails";
  }


}
