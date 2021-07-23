package com.example.petProject.Dto;

import com.example.petProject.model.enums.Status;
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
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TaskDto {

  private long id;
  @NotNull
  private String name;
  @NotNull
  private Status status;
  @NotNull
  private String description;
  @NotNull
  private Date deadline;
  private int price;
}
