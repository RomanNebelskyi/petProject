package com.example.petProject.model;

import com.example.petProject.Dto.TaskDto;
import com.example.petProject.model.enums.Status;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue
  private long id;
  @Enumerated(EnumType.STRING)
  private Status status;
  private String name;
  private String description;
  private Date deadline;
  private int price;
  private int code;
  @OneToMany(mappedBy="currentTask", fetch = FetchType.EAGER)
  private List<User> workers;
  @ManyToOne(fetch = FetchType.EAGER)
  private User buyer;


  public TaskDto toDto()
  {
    TaskDto taskDto = new TaskDto();
    taskDto.setDeadline(this.getDeadline());
    taskDto.setDescription(this.getDescription());
    taskDto.setStatus(this.getStatus());
    taskDto.setPrice(this.getPrice());
    taskDto.setId(this.getId());
    taskDto.setName(this.getName());
    return taskDto;
  }



}
