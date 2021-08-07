package com.example.petProject.model;


import com.example.petProject.Dto.UserDto;
import com.example.petProject.model.enums.Role;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usr")
public class User implements UserDetails, Comparable<User> {

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    @Email
    private String email;
    private String name;
    private String pass;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isBanned;
    private String confirmationCode;
    private LocalDateTime registrationDate;
    @OneToMany(mappedBy = "buyer", fetch = FetchType.EAGER)
    private List<Task> tasks;
    @ManyToOne(fetch = FetchType.EAGER)
    private Task currentTask; //

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }


    @Override
    public String getPassword() {
        return this.getPass();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isBanned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getConfirmationCode().equals("");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", role=" + role +
                ", confirmationCode='" + confirmationCode + '\'' +
                ", registrationDate=" + registrationDate;
    }

    public UserDto toDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail(userDto.getEmail());
        userDto.setName(this.getName());
        userDto.setId(this.getId());
        userDto.setRole(this.getRole());
        userDto.setRegistrationDate(this.getRegistrationDate());
        return userDto;
    }



    @Override
    public int compareTo(User o) {
        return this.getName().compareTo(o.getName());
    }
}
