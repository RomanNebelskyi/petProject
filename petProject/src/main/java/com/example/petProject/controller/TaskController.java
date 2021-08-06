package com.example.petProject.controller;

import com.example.petProject.Dto.TaskDto;
import com.example.petProject.model.Task;
import com.example.petProject.model.TaskQueue;
import com.example.petProject.model.User;
import com.example.petProject.model.enums.Role;
import com.example.petProject.model.enums.Status;
import com.example.petProject.repo.TaskQueueRepo;
import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import com.example.petProject.service.FileService;
import com.example.petProject.service.MailService;
import com.google.common.io.Files;
import java.nio.file.FileSystemNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final MailService mailService;
    private final TaskQueueRepo taskQueueRepo;
    private final FileService fileService;
    private final String DIR_PATH = "D:\\petProject\\petProject\\techTasks";

    public TaskController(TaskRepo taskRepo, UserRepo userRepo,
            MailService mailService, TaskQueueRepo taskQueueRepo,
            FileService service) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.mailService = mailService;
        this.taskQueueRepo = taskQueueRepo;
        this.fileService = service;
    }

    @GetMapping("/tech-req/delete")
    public String deleteFile(@RequestParam("id") long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Sorry, but task can*t be found"));
        if (!fileService.deleteFile(task.getTechReq(), DIR_PATH)) {
            throw new FileSystemNotFoundException("Can*t delete file");
        }
        task.setTechReq(null);
        taskRepo.save(task);
        return "redirect:/cabinet";
    }

    @PostMapping("/tech-req/edit")
    public String editFile(Model model, @RequestParam("id") long id,
            @RequestParam("techreq") MultipartFile file) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sorry, but task can*t be found"));
        User buyer = task.getBuyer();

        if (!fileService.deleteFile(task.getTechReq(), DIR_PATH)) {
            throw new FileSystemNotFoundException("Can*t delete file");
        }

        if (file != null) {

            String fileExtension = Files.getFileExtension(file.getOriginalFilename());

            boolean checkExtension =
                    !"pdf".equals(fileExtension) && !"docx".equals(fileExtension) && !"doc"
                            .equals(fileExtension);

            if (checkExtension) {
                model.addAttribute("error_message",
                        "Sorry, but file format can only be .doc, .docx or .pdf");

                return "redirect:/cabinet";
            }
            String fullFileName =
                    UUID.randomUUID().toString().concat("_" + buyer.getEmail()) + "."
                            + fileExtension;

            task.setTechReq(fileService.saveFile(file, DIR_PATH, fullFileName));
        }
        taskRepo.save(task);
        return "redirect:/cabinet";
    }

    @PostMapping("/tech-req/add")
    public String addFile(Model model, @RequestParam("id") long id,
            @RequestParam("techreq") MultipartFile file) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sorry, but task can*t be found"));
        User buyer = task.getBuyer();

        if (file != null) {

            String fileExtension = Files.getFileExtension(file.getOriginalFilename());

            boolean checkExtension =
                    !"pdf".equals(fileExtension) && !"docx".equals(fileExtension) && !"doc"
                            .equals(fileExtension);

            if (checkExtension) {
                model.addAttribute("error_message",
                        "Sorry, but file format can only be .doc, .docx or .pdf");

                return "redirect:/cabinet";
            }
            String fullFileName =
                    UUID.randomUUID().toString().concat("_" + buyer.getEmail()) + "."
                            + fileExtension;

            task.setTechReq(fileService.saveFile(file, DIR_PATH, fullFileName));
        }
        taskRepo.save(task);
        return "redirect:/cabinet";
    }

    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
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

    @GetMapping("/start")
    public String startTask(@RequestParam("id") long taskId, Model model) {

        Task task = taskRepo.findById(taskId).orElseThrow(
                () -> new NoSuchElementException("TaskDto with id " + taskId + " can*t be found!"));
        TaskQueue queue = taskQueueRepo.findByTaskId((int) taskId).orElseThrow(
                () -> new NoSuchElementException("TaskDto with id " + taskId + " can*t be found!"));

        if (queue.getPosition() == 1) {
            task.setStatus(Status.IN_PROCESS);
            taskRepo.save(task);
            taskQueueRepo.delete(queue);
            taskQueueRepo.findAll().stream()
                    .peek(e -> e.setPosition(e.getPosition() - 1))
                    .forEach(taskQueueRepo::save);

        } else {
            model.addAttribute("error_message",
                    "Sorry, but this task*s position is not 1 in the queue");
        }
        return "redirect:/task/details?id=" + task.getId();
    }

    @GetMapping("/finish")
    public String finishTask(@RequestParam("id") long taskId, Model model) {

        Task task = taskRepo.findById(taskId).orElseThrow(
                () -> new NoSuchElementException("TaskDto with id " + taskId + " can*t be found!"));
        task.setStatus(Status.DONE);
        List<User> workers = task.getWorkers();
        workers.stream().peek(e -> e.setCurrentTask(null)).forEach(userRepo::save);
        task.setWorkers(null);
        taskRepo.save(task);
        return "redirect:/task/details?id=" + task.getId();
    }

    @GetMapping("/add")
    public String page() {
        return "addTask";
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasAnyAuthority({'USER', 'MODERATOR','ADMIN'})")
    public String cancelTask(@RequestParam("id") long taskId) {

        Task task = taskRepo.findById(taskId).orElseThrow(
                () -> new NoSuchElementException("TaskDto with id " + taskId + " can*t be found!"));
        task.setStatus(Status.CANCELLED);
        if (task.getWorkers() != null && task.getWorkers().size() != 0) {
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
                    .forEach(taskQueueRepo::save);
        }
        return "redirect:/cabinet";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority({'USER', 'MODERATOR'})")
    public String addTask(Model model,
            @RequestParam("name") String name,
            @RequestParam("deadline")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deadline,
            @RequestParam("price") int price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "techreq", required = false) MultipartFile file) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            if (user.getRole() != Role.USER && user.getRole() != Role.MODERATOR) {
                throw new IllegalAccessException(
                        "Only User or Moderator can add tasks!" + user.toString());
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
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getName());

        if (file.getSize() > 0) {
            String fileExtension = Files.getFileExtension(file.getOriginalFilename());

            boolean checkExtension =
                    !"pdf".equals(fileExtension) && !"docx".equals(fileExtension) && !"doc"
                            .equals(fileExtension);

            if (checkExtension) {
                model.addAttribute("error_message",
                        "Sorry, but file format can only be .doc, .docx or .pdf");
                return "redirect:/cabinet";
            }

            String fullFileName =
                    UUID.randomUUID().toString().concat("_" + user.getEmail()) + "."
                            + fileExtension;

            task.setTechReq(fileService.saveFile(file, DIR_PATH, fullFileName));
        }
        taskRepo.save(task);
        return "redirect:/cabinet";
    }

    @GetMapping("/details")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    public String details(Model model,
            @RequestParam("id") long id,
            @RequestParam(value = "error_message", required = false) String error) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Task with Id = " + id + " can*t be found!"));
        model.addAttribute("task", task);
        model.addAttribute("workers", task.getWorkers());
        model.addAttribute("freeWorkers", userRepo.findByRoleAndCurrentTaskNull(Role.PROGRAMMER));
        if (error != null) {
            model.addAttribute("error_message", error);
        }
        return "taskDetails";
    }

    @GetMapping("/submit")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    public String submit(Model model, @RequestParam("id") long taskId) {

        Task currentTask = taskRepo.findById(taskId).orElseThrow(
                () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));

        if (currentTask.getWorkers() == null || currentTask.getWorkers().size() == 0) {
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
                .sendConfirmationCode(currentTask, currentTask.getBuyer().getEmail(),
                        code.toString(),
                        taskId);
        return "redirect:/moder";
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/submit-code")
    public String receiveCode(@RequestParam("taskId") long taskId,
            Model model) {

        model.addAttribute("taskId", taskId);
        Task task = taskRepo.findById(taskId).orElseThrow(
                () -> new NoSuchElementException("Task with id=" + taskId + " can*t be found"));
        if (task != null) {
            TaskDto taskDto = task.toDto();
            model.addAttribute("task", taskDto);
            model.addAttribute("buyerName", task.getBuyer().getName());
        }
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
        }
        return "redirect:/cabinet";
    }
}
