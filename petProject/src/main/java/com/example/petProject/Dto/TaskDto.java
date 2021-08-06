package com.example.petProject.Dto;

import com.example.petProject.model.enums.Status;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskDto {

    private long id;
    @NotNull
    private String name;
    private String techReq;
    @NotNull
    private Status status;
    @NotNull
    private String description;
    @NotNull
    private Date deadline;
    private int price;
}
