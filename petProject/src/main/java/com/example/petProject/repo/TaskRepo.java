package com.example.petProject.repo;

import com.example.petProject.model.Task;
import com.example.petProject.model.User;
import com.example.petProject.model.enums.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {

  public List<Task> getAllByBuyerId(long id);

  public List<Task> findAllByBuyer(User buyer);

  public List<Task> findByStatusLike(Status status);

}
