package com.example.petProject.service;

import com.example.petProject.model.Task;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  @Autowired
  private JavaMailSender sender;

  public void sendConfirmationCode(String code, String to) {

    String message =
        "Hello, please confirm your account:\nVisit this link:\nhttp://localhost:8080/registration/confirm/"
            + code;
    String subject = "Confirm your email on Twitter app";

    sendMail(to, subject, message);
  }


  public void sendConfirmationCode(Task task, String to, String code, long id) {

    String message = "Hello, please confirm your task:\n"
        + "CODE - " + code + "\n"
        + "Visit this link: \nhttp://localhost:8080/task/submit-code?taskId=" + id;

    String subject = "Confirm your task!";

    sendMail(to, subject, message);
  }

  public void sendNewPass(String to, String pass) {

    String message = "Hello, your new password is : \n" + pass;
    String subject = "New password!";
    sendMail(to, subject, message);
  }

  public void sendMail(
      String to,
      String subject,
      String text
  ) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setText(text);
    message.setSubject(subject);
    message.setFrom("noreply@gmail.com");
    try {
      sender.send(message);
    } catch (MailAuthenticationException e) {
      e.printStackTrace();
    }


  }


}
