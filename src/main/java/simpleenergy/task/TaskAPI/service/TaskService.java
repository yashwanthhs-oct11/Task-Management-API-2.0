package simpleenergy.task.TaskAPI.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import simpleenergy.task.TaskAPI.model.Task;
import simpleenergy.task.TaskAPI.enums.TaskStatus;
import simpleenergy.task.TaskAPI.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(String id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setPriority(taskDetails.getPriority());
        task.setStatus(taskDetails.getStatus());
        task.setDueDate(taskDetails.getDueDate());
        return taskRepository.save(task);
    }

    public Task updateStatus(String id, TaskStatus taskStatus) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getDependencies() != null && !task.getDependencies().isEmpty()) {
            Optional<Task> dependencyTask = taskRepository.findById(task.getDependencies());

            if (dependencyTask.isPresent()) {
                TaskStatus dependencyStatus = dependencyTask.get().getStatus();
                if (dependencyStatus == TaskStatus.COMPLETE) {
                    task.setStatus(taskStatus);
                    taskRepository.save(task);
                } else {
                    throw new RuntimeException("Cannot update task status because dependency is not complete");
                }
            } else {
                throw new RuntimeException("Dependency task not found");
            }
        } else {
            task.setStatus(taskStatus);
            taskRepository.save(task);
        }
        return task;
    }
    // Filter tasks by priority
    public List<Task> filterByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    // Filter tasks by due date (upcoming, overdue)
    public List<Task> filterByDueDate(LocalDate dueDate) {
        return taskRepository.findByDueDate(dueDate);
    }

    // Filter tasks by status
    public List<Task> filterByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    // Filter tasks by assigned user
    public List<Task> filterByAssignedUser (String assignedUser ) {
        return taskRepository.findByAssignedUser (assignedUser );
    }

    // Sort tasks by priority
    public List<Task> sortByPriority() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "priority"));
    }

    // Sort tasks by due date
    public List<Task> sortByDueDate() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "dueDate"));
    }

    // Sort tasks by creation date
    public List<Task> sortByCreationDate() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt")); // Assuming you have a createdAt field
    }

    // Advanced query: Retrieve all high-priority, incomplete tasks for a specific team member due within the next three days
    public List<Task> getHighPriorityIncompleteTasksForUser (String assignedUser ) {
        LocalDate today = LocalDate.now();
        LocalDate threeDaysFromNow = today.plusDays(3);
        return taskRepository.findByPriorityAndStatusAndAssignedUserAndDueDateBetween(
                "HIGH", TaskStatus.INCOMPLETE, assignedUser , today, threeDaysFromNow);
    }
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndStatus(LocalDateTime.now(), "Pending");
    }
}
