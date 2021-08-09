package com.example.petProject.repo;

import com.example.petProject.model.TaskQueue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskQueueRepo extends JpaRepository<TaskQueue, Long> {

    public List<TaskQueue> getByPositionGreaterThan(int position);

    public Optional<TaskQueue> findByTaskId(int taskId);

    public Optional<TaskQueue> findByPosition(int position);

}
