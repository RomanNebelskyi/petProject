package com.example.petProject.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Autowired
  private MailProperties properties;


  @Bean
  public JavaMailSender mailSender() {

    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setPassword("1111zzzz");
    sender.setUsername("zieghhhhhhhhhh@gmail.com");
    sender.setPort(587);
    sender.setHost("smtp.gmail.com");

    Properties properties = sender.getJavaMailProperties();
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.debug", "true");

    return sender;
  }

}
