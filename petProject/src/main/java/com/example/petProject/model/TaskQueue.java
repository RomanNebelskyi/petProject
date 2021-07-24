package com.example.petProject.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class TaskQueue {

  @Id
  @GeneratedValue
  private long id;
  private int position;
  private int taskId;

}
