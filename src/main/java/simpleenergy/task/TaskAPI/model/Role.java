package simpleenergy.task.TaskAPI.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import simpleenergy.task.TaskAPI.enums.Roles;

@Document(collection = "roles")
@Data
public class Role {

    @Id
    private String id;
    private Roles role;
}
