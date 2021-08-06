package com.example.petProject.Dto;


import com.example.petProject.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserDto {

    private long id;
    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private Role role;
    @NotNull
    @JsonIgnore
    private LocalDateTime registrationDate;

}
