package com.example.petProject.service;

import com.example.petProject.Dto.TaskDto;
import com.example.petProject.model.Task;
import com.example.petProject.model.User;
import java.util.Comparator;
import org.springframework.stereotype.Service;

@Service
public class ComparatorService {

    public static Comparator<Task> getTaskComparator(String toSort, String flow) {
        Comparator<Task> comparator = null;
        if (toSort == null || flow == null) {
            comparator = Comparator.comparing(Task::getId);
            return comparator;
        }
        switch (toSort) {
            case "id": {
                comparator = Comparator.comparing(Task::getId);
                break;
            }
            case "name": {
                comparator = Comparator.comparing(Task::getName);
                break;
            }
            case "buyer": {
                comparator = Comparator.comparing(e -> e.getBuyer().getName());
                break;
            }
            case "status": {
                comparator = Comparator.comparing(e -> e.getStatus().toString());
                break;
            }
            case "deadline": {
                comparator = Comparator.comparing(Task::getDeadline);
                break;
            }
            case "price": {
                comparator = Comparator.comparing(Task::getPrice);
                break;
            }
            case "description": {
                comparator = Comparator.comparing(Task::getDescription);
                break;
            }
            case "workers": {
                comparator = Comparator.comparing(a -> a.getWorkers().size());
                break;
            }
            default:
                comparator = Comparator.comparing(Task::getId);
                break;
        }

        if (flow.equals("DESC")) {
            comparator = comparator.reversed();
        }
        return comparator;

    }

    public static Comparator<TaskDto> getDtoComparator(String toSort, String flow) {
        Comparator<TaskDto> comparator = null;
        if (toSort == null || flow == null) {
            comparator = Comparator.comparing(TaskDto::getName);
            return comparator;
        }
        switch (toSort) {

            case "status": {
                comparator = Comparator.comparing(e -> e.getStatus().toString());
                break;
            }
            case "deadline": {
                comparator = Comparator.comparing(TaskDto::getDeadline);
                break;
            }
            case "price": {
                comparator = Comparator.comparing(TaskDto::getPrice);
                break;
            }
            case "description": {
                comparator = Comparator.comparing(TaskDto::getDescription);
                break;
            }
            default: {
                comparator = Comparator.comparing(TaskDto::getName);
                break;
            }

        }

        if (flow.equals("DESC")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    public static Comparator<User> getUserComparator(String toSort, String flow) {
        Comparator<User> comparator = null;
        if (toSort == null || flow == null) {
            comparator = Comparator.comparing(User::getId);
            return comparator;
        }

        switch (toSort) {
            case "id": {
                comparator = Comparator.comparing(User::getId);
                break;
            }
            case "name": {
                comparator = Comparator.comparing(User::getName);
                break;
            }
            case "email": {
                comparator = Comparator.comparing(User::getEmail);
                break;
            }
            case "role": {
                comparator = Comparator.comparing(e -> e.getRole().toString());
                break;
            }
            case "registationDate": {
                comparator = Comparator.comparing(User::getRegistrationDate);
                break;
            }
            default:
                comparator = Comparator.comparing(User::getName);
                break;
        }
        if (flow.equals("DESC")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
