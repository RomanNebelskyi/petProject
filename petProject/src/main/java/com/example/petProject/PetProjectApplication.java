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


  public static void main(String[] args) {
     SpringApplication.run(PetProjectApplication.class, args);

  }

}
