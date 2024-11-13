package simpleenergy.task.TaskAPI.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import simpleenergy.task.TaskAPI.model.Task;
import simpleenergy.task.TaskAPI.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByPriority(String priority);
    List<Task> findByDueDate(LocalDate dueDate);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByAssignedUser (String assignedUser );
    List<Task> findByPriorityAndStatusAndAssignedUserAndDueDateBetween(
            String priority, TaskStatus status, String assignedUser , LocalDate startDate, LocalDate endDate);
    List<Task> findByDueDateBeforeAndStatus(LocalDateTime dueDate, String status);
}

