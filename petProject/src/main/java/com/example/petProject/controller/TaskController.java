package com.example.petProject.controller;

import ch.qos.logback.core.joran.spi.ActionException;
import com.example.petProject.model.Task;
import com.example.petProject.model.TaskQueue;
import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.model.enums.Status;
import com.example.petProject.repo.TaskQueueRepo;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import com.example.petProject.service.MailService;
import com.sun.xml.bind.v2.TODO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
  private final MailService mailService;
  @Autowired
  private TaskQueueRepo taskQueueRepo;

  public TaskController(TaskRepo taskRepo, UserRepo userRepo, MailService mailService) {
    this.taskRepo = taskRepo;
    this.userRepo = userRepo;
    this.mailService = mailService;
  }

  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN', 'MAIN_ADMIN')")
  @PostMapping("/change")
  public String change(
      @RequestParam("id") long taskId,
      @RequestParam("name") String name,
      @RequestParam(value = "deadline", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deadline,
      @RequestParam("price") int price,
      @RequestParam(value = "description", required = false) String description) {

    Task task = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));

    task.setName(name);
    task.setDescription(description);
    task.setPrice(price);
    if (deadline != null) {
      task.setDeadline(deadline);
    }
    taskRepo.save(task);

    return "redirect:/task/details?id=" + taskId;
  }

  @GetMapping("/add")
  public String page() {
    return "addTask";
  }

  @GetMapping("/cancel")
  @PreAuthorize("hasAnyAuthority({'USER', 'MODERATOR'})")
  public String cancelTask(@RequestParam("id") long taskId) {

    Task task = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("TaskDto with id " + taskId + " can*t be found!"));
    task.setStatus(Status.CANCELLED);
    if (task.getWorkers() != null) {
      task.getWorkers().forEach(e -> e.setCurrentTask(null));
    }

    task.setWorkers(null);
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    task.setBuyer(user);
    taskRepo.save(task);

    Optional<TaskQueue> taskQueue = taskQueueRepo.findByTaskId((int) taskId);

    if (taskQueue.isPresent()) { // We can cancel it when we have "ESTIMATED"
      List<TaskQueue> toChange = taskQueueRepo
          .getByPositionGreaterThan(taskQueue.get().getPosition());
      taskQueueRepo.delete(taskQueue.get());

      toChange.stream()
          .peek(e -> e.setPosition(e.getPosition() - 1))
          .forEach(e -> taskQueueRepo.save(e));

    }

    return "redirect:/cabinet";
  }

  @PostMapping("/add")
  @PreAuthorize("hasAnyAuthority({'USER', 'MODERATOR'})")
  public String addTask(
      @RequestParam("name") String name,
      @RequestParam("deadline")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deadline,
      @RequestParam("price") int price,
      @RequestParam(value = "description", required = false) String description) {

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
    if (description == null) {
      task.setDescription("");
    } else {
      task.setDescription(description);
    }
    task.setStatus(Status.ESTIMATED);
    task.setDeadline(deadline);
    task.setName(name);

    taskRepo.save(task);
    return "redirect:/cabinet";
  }

  @GetMapping("/details")
  @PreAuthorize("hasAuthority('MODERATOR')")
  public String details(Model model,
      @RequestParam("id") long id,
      @RequestParam(value = "error_message", required = false) String error) {
//HERE DROPPER EXCEPTION
    Task task = taskRepo.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Task with Id = " + id + " can*t be found!"));
    model.addAttribute("task", task);
    model.addAttribute("workers", task.getWorkers());
    model.addAttribute("freeWorkers", userRepo.findByRoleAndCurrentTaskNull(Role.PROGRAMMER));
    if (error != null) {
      model.addAttribute("error_message", error);
    }
    //////////////////////////changed add-task to addTask!!!!!!!!!!;

    return "taskDetails";
  }

  @GetMapping("/submit")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN', 'MAIN_ADMIN')")
  public String submit(Model model, @RequestParam("id") long taskId) {

    Task currentTask = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));

    if (currentTask.getWorkers() == null) {

      model.addAttribute("error_message",
          "Sorry, but you can*t go to next step because there are 0 workers");
      //DELETE
      System.out.println("here");
      return "redirect:/task/details?id=" + taskId;

    }

    if (currentTask.getWorkers().size() == 0) {
      model.addAttribute("error_message",
          "Sorry, but you can*t go to next step because there are 0 workers");
      return "redirect:/task/details?id=" + taskId;
    }

    currentTask.setStatus(Status.WAITING_ACCEPT);

    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      code.append(ThreadLocalRandom.current().nextInt(0, 10));
    }

    currentTask.setCode(Integer.parseInt(code.toString()));
    taskRepo.save(currentTask);
    mailService
        .sendConfirmationCode(currentTask, currentTask.getBuyer().getEmail(), code.toString(),
            taskId);
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!! redirect???
    return "redirect:/moder";
  }


  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/submit-code")
  public String receiveCode(@RequestParam("taskId") long taskId,
      Model model) {

    model.addAttribute("taskId", taskId);

    return "submitCode";
  }

  @PreAuthorize("hasAuthority('USER')")
  @PostMapping("/submit-code")
  public String confirmCode(Model model, @RequestParam("taskId") long taskId,
      @RequestParam("code") int code) {

    Task currentTask = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));

    if (currentTask.getCode() != code) {
      model.addAttribute("error_message", "Codes are not equal. Please repeat action");

      currentTask.setCode(0);
      taskRepo.save(currentTask);
      //////////// ADD TIME
      ////TODO from queue to in_process
    } else {
      Task task = taskRepo.findById(taskId).orElseThrow(
          () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));
      task.setStatus(Status.QUEUED);
      task.setCode(0);
      taskRepo.save(task);

      TaskQueue queue = new TaskQueue();
      queue.setTaskId((int) taskId);
      queue.setPosition(taskQueueRepo.findAll().size() + 1);

      Optional<TaskQueue> alreadyExist = taskQueueRepo.findByTaskId((int) taskId);

      if (alreadyExist.isPresent()) {
        try {
          throw new IllegalAccessException("Already in queue!");
        } catch (IllegalAccessException exception) {
          exception.printStackTrace();
        }

      }

      taskQueueRepo.save(queue);
      //set task in queue

    }

    //check if time is valid
    return "redirect:/cabinet";
  }


}
