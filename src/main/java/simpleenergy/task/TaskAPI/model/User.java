package simpleenergy.task.TaskAPI.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import simpleenergy.task.TaskAPI.enums.Roles;

import java.util.List;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private Roles roles;
    private List<Team> teams;
    private String email;
}
