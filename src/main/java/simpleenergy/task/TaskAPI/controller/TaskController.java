package simpleenergy.task.TaskAPI.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import simpleenergy.task.TaskAPI.model.Task;
import simpleenergy.task.TaskAPI.enums.TaskStatus;
import simpleenergy.task.TaskAPI.service.TaskService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Log log = LogFactory.getLog(TaskController.class);
    @Autowired
    private TaskService taskService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/create")
    public Task createTask(@RequestBody Task task) {
        Task newTask = taskService.createTask(task);
        messagingTemplate.convertAndSend("/topic/task-updates", newTask);
        return newTask;
    }

    @PutMapping("/{taskId}/update-status")
    public Task updateTaskStatus(@PathVariable String taskId, @RequestBody TaskStatus status) {
        Task updatedTask = taskService.updateStatus(taskId, status);
        messagingTemplate.convertAndSend("/topic/task-updates", updatedTask);
        return updatedTask;
    }

    @GetMapping("/allTasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@RequestParam String id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@RequestParam String id, @RequestBody Task taskDetails) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@RequestParam String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // Filter tasks by priority
    @GetMapping("/filter/priority")
    public ResponseEntity<List<Task>> filterByPriority(@RequestParam String priority) {
        List<Task> tasks = taskService.filterByPriority(priority);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Filter tasks by due date
    @GetMapping("/filter/due-date")
    public ResponseEntity<List<Task>> filterByDueDate(@RequestParam LocalDate dueDate) {
        List<Task> tasks = taskService.filterByDueDate(dueDate);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Filter tasks by status
    @GetMapping("/filter/status")
    public ResponseEntity<List<Task>> filterByStatus(@RequestParam TaskStatus status) {
        List<Task> tasks = taskService.filterByStatus(status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Filter tasks by assigned user
    @GetMapping("/filter/assigned-user")
    public ResponseEntity<List<Task>> filterByAssignedUser(@RequestParam String assignedUser ) {
        List<Task> tasks = taskService.filterByAssignedUser (assignedUser );
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Sort tasks by priority
    @GetMapping("/sort/priority")
    public ResponseEntity<List<Task>> sortByPriority() {
        List<Task> tasks = taskService.sortByPriority();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Sort tasks by due date
    @GetMapping("/sort/due-date")
    public ResponseEntity<List<Task>> sortByDueDate() {
        List<Task> tasks = taskService.sortByDueDate();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Sort tasks by creation date
    @GetMapping("/sort/creation-date")
    public ResponseEntity<List<Task>> sortByCreationDate() {
        List<Task> tasks = taskService.sortByCreationDate();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Advanced query: Retrieve all high-priority, incomplete tasks for a specific team member due within the next three days
    @GetMapping("/advanced-query")
    public ResponseEntity<List<Task>> getHighPriorityIncompleteTasksForUser(@RequestParam String assignedUser ) {
        List<Task> tasks = taskService.getHighPriorityIncompleteTasksForUser (assignedUser );
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}