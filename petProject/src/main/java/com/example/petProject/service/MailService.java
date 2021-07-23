package com.example.petProject.service;

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
