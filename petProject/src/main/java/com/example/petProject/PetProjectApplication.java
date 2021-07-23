package com.example.petProject;

import com.example.petProject.repo.TaskRepo;
import com.example.petProject.repo.UserRepo;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PetProjectApplication {
@Autowired
  private static TaskRepo taskRepo;



  public static void main(String[] args) {
    ConfigurableApplicationContext run = SpringApplication.run(PetProjectApplication.class, args);
    TaskRepo bean = run.getBean(TaskRepo.class);
    UserRepo userRepo = run.getBean(UserRepo.class);
   // System.out.println(bean.getAllByBuyerId(userRepo.findAll().get(0).getId()));
    //System.out.println(taskRepo.getAllByBuyerId(21));
  }

}
