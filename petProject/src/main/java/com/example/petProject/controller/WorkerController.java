package com.example.petProject.controller;

import com.example.petProject.model.Task;
import com.example.petProject.model.User;
import com.example.petProject.model.enums.Status;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/worker")
@PreAuthorize("hasAnyAuthority('MODERATOR','ADMIN','MAIN_ADMIN')")
public class WorkerController {


  private final UserRepo userRepo;
  private final TaskRepo taskRepo;

  public WorkerController(UserRepo userRepo, TaskRepo taskRepo) {
    this.userRepo = userRepo;
    this.taskRepo = taskRepo;
  }

  @GetMapping("/add")
  public String add(Model model, @RequestParam("worker") long workerId,
      @RequestParam("task") long taskId) {

    User worker = userRepo.findById(workerId).orElseThrow(
        () -> new UsernameNotFoundException("worker with id=" + workerId + " can*t be found"));
    Task currentTask = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("task with id=" + taskId + " can*t be found"));

    boolean checkStatus = currentTask.getStatus() == Status.ESTIMATED
        | currentTask.getStatus() == Status.WAITING_ACCEPT |
        currentTask.getStatus() == Status.QUEUED | currentTask.getStatus() == Status.IN_PROCESS;

    if (checkStatus) {
      List<User> workers = currentTask.getWorkers();
      if (workers == null) {
        workers = new ArrayList<>();
      }
      worker.setCurrentTask(currentTask);
      workers.add(worker);
      currentTask.setWorkers(workers);
      userRepo.save(worker);
      taskRepo.save(currentTask);
    } else {
      String message = new String( "Sorry, but you can*t change team of developers on this stage of developing");

      model.addAttribute("error_message",
          message);
    }
    return "redirect:/task/details?id=" + taskId;
  }

  @GetMapping("/delete")
  public String delete(Model model, @RequestParam("worker") long workerId,
      @RequestParam("task") long taskId) {

    User worker = userRepo.findById(workerId).orElseThrow(
        () -> new UsernameNotFoundException("Worker with id=" + workerId + " can*t be found"));
    Task task = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("task with id=" + taskId + " can*t be found"));

    boolean checkStatus = task.getStatus() == Status.ESTIMATED
        | task.getStatus() == Status.WAITING_ACCEPT |
        task.getStatus() == Status.QUEUED | task.getStatus() == Status.IN_PROCESS;

    if (checkStatus) {

      worker.setCurrentTask(null);
      List<User> workers = task.getWorkers();
      workers.remove(worker);
      task.setWorkers(workers);
      userRepo.save(worker);
      taskRepo.save(task);
    } else {
      String message = new String( "Sorry, but you can*t change team of developers on this stage of developing");
      model.addAttribute("error_message",
          message);

    }

    // redirect ????????????????
    return "redirect:/task/details?id=" + taskId;

  }

}
