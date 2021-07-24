package com.example.petProject.controller;

import com.example.petProject.model.Task;
import com.example.petProject.model.TaskQueue;
import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.model.enums.Status;
import com.example.petProject.repo.TaskQueueRepo;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import com.example.petProject.service.MailService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
      @RequestParam("deadline")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deadline,
      @RequestParam("price") int price,
      @RequestParam("description") String description) {

    Task task = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));

    task.setName(name);
    task.setDescription(description);
    task.setPrice(price);
    task.setDeadline(deadline);
    taskRepo.save(task);

    return "redirect:/details?id=" + taskId;
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


    TaskQueue queuedTask = taskQueueRepo.findByTaskId((int) taskId).orElseThrow(
        () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));

    List<TaskQueue> toChange = taskQueueRepo
        .getByPositionGreaterThan(queuedTask.getPosition());

    taskQueueRepo.delete(queuedTask);

    toChange.stream()
        .peek(e -> e.setPosition(e.getPosition() - 1))
        .forEach(e -> taskQueueRepo.save(e));



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

  @GetMapping("/details")
  @PreAuthorize("hasAuthority('MODERATOR')")
  public String details(Model model,
      @RequestParam("id") long id,
      @RequestParam(value = "error_message", required = false) String error) {

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

  @GetMapping("/submit?id={id}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN', 'MAIN_ADMIN')")
  public String submit(Model model, @PathVariable("id") long taskId, TimeZone timeZone,
      HttpServletRequest request) {

    Task currentTask = taskRepo.findById(taskId).orElseThrow(
        () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));

    if (currentTask.getWorkers() == null) {

      model.addAttribute("error_message",
          "Sorry, but you can*t go to next step because there are 0 workers");

      return "redirect:/task/details?id=" + taskId;

    }

    if (currentTask.getWorkers().size() == 0) {
      model.addAttribute("error_message",
          "Sorry, but you can*t go to next step because there are 0 workers");

      return "redirect:/task/details?id=" + taskId;

    }

    currentTask.setStatus(Status.IN_PROCESS);

    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      code.append(ThreadLocalRandom.current().nextInt(0, 10));
    }
    long timestamp = 0;

    if (timeZone != null) {
      timestamp = LocalDateTime.now().atZone(ZoneId.of(timeZone.getID())).toEpochSecond();
    } else {
      if (request != null) {
        timestamp = request.getDateHeader("x-forwarded-for");
      } else {
        timestamp = System.currentTimeMillis();
      }
    }

    mailService
        .sendConfirmationCode(currentTask, currentTask.getBuyer().getEmail(), code.toString(),
            timestamp, taskId);
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!! redirect???
    return "redirect:/moder";
  }

  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/submit?code={code}&timestamp={timestamp}&id={taskId}")
  public String receiveCode(Model model, @PathVariable("code") String code,
      @PathVariable("timestamp") long timestamp, @PathVariable("taskId") long taskId,
      TimeZone timeZone) {

    if (timeZone != null) {
      LocalDateTime time = Instant.ofEpochMilli(timestamp).atZone(
          ZoneId.of(timeZone.getID())).toLocalDateTime();
      return checkTimeExpired(model, taskId, time);
    } else {
      LocalDateTime time = Instant.ofEpochMilli(timestamp).atZone(
          ZoneId.systemDefault()).toLocalDateTime();
      return checkTimeExpired(model, taskId, time);
    }
  }

  private String checkTimeExpired(Model model,
      long taskId,
      LocalDateTime time) {
    if (time.plus(5, ChronoUnit.MINUTES).compareTo(LocalDateTime.now()) < 0) {
      model.addAttribute("error_message", "Time out. Please repeat action");

    } else {
      Task task = taskRepo.findById(taskId).orElseThrow(
          () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));
      task.setStatus(Status.QUEUED);
      taskRepo.save(task);

      TaskQueue queue = new TaskQueue();
      queue.setTaskId((int) taskId);
      queue.setPosition(taskQueueRepo.findAll().size() + 1);
      taskQueueRepo.save(queue);
      //set task in queue

    }
    return "redirect:/cabinet";
  }

}
