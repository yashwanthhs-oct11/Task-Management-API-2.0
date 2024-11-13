package simpleenergy.task.TaskAPI.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import simpleenergy.task.TaskAPI.enums.TaskStatus;

import java.util.List;

@Document(collection = "tasks")
@Data
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private String priority;
    private TaskStatus status;
    private String dueDate;
    private String assignedUser; //userId
    private List<String> collaborators;
    private String dependencies;
    private Team team;
}
