package simpleenergy.task.TaskAPI.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import simpleenergy.task.TaskAPI.model.Team;

import java.util.Optional;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {

    // Find a team by its ID
    Optional<Team> findById(String id);

    // Find a team by its name
    Optional<Team> findByName(String name);

}
