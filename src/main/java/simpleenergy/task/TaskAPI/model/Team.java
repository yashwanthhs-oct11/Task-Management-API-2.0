package simpleenergy.task.TaskAPI.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "teams")
@Data
public class Team {

    @Id
    private String id;
    private String name;
    private User owner; // Team owner with full access
    private List<User> admins; // Admins can manage tasks
    private List<User> members; // Members with limited access
    private List<Task> tasks;
}
