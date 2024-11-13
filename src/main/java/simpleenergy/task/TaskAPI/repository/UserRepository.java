package simpleenergy.task.TaskAPI.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import simpleenergy.task.TaskAPI.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Find a user by ID
    Optional<User> findById(String id);

    // Find a user by username (if applicable)
    Optional<User> findByUsername(String username);

//    // Find users by role (if needed for role-based queries)
//    List<User> findByRoles(String role);
}
