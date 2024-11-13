package simpleenergy.task.TaskAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simpleenergy.task.TaskAPI.model.Team;
import simpleenergy.task.TaskAPI.model.User;
import simpleenergy.task.TaskAPI.enums.Roles;
import simpleenergy.task.TaskAPI.repository.TeamRepository;
import simpleenergy.task.TaskAPI.repository.UserRepository;

import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a team
    public Team createTeam(String teamName) {
        Team team = new Team();
        team.setName(teamName);
        return teamRepository.save(team);
    }

    // Add user to team and assign a role
    @Transactional
    public Team addUserToTeam(String teamId, String userId, Roles role) {
        Team team = getTeamById(teamId);

        if (team.getMembers().isEmpty()) {
            // If the team is empty, assign the owner role to the first user added
            User user = getUserById(userId);
            user.setRoles(Roles.OWNER);  // Automatically set first user as OWNER
            team.getMembers().add(user);
            return teamRepository.save(team);
        }

        User user = getUserById(userId);
        user.setRoles(role);
        team.getMembers().add(user);
        return teamRepository.save(team);
    }

    // Remove user from team
    @Transactional
    public Team removeUserFromTeam(String teamId, String userId) {
        Team team = getTeamById(teamId);
        User user = getUserById(userId);
        if (team.getMembers().contains(user)) {
            team.getMembers().remove(user);
            return teamRepository.save(team);
        } else {
            throw new RuntimeException("User not found in this team");
        }
    }

    // Assign a role to a user in the team
    @Transactional
    public Team assignRoleToUser(String teamId, String userId, Roles role) {
        Team team = getTeamById(teamId);
        User user = getUserById(userId);

        if (team.getMembers().contains(user)) {
            user.setRoles(role);
            return teamRepository.save(team);
        } else {
            throw new RuntimeException("User not found in this team");
        }
    }

    // Get users in a team
    public Team getUsersInTeam(String teamId) {
        return getTeamById(teamId);
    }

    // Helper method to get team by ID
    private Team getTeamById(String teamId) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (teamOpt.isPresent()) {
            return teamOpt.get();
        } else {
            throw new RuntimeException("Team not found with id " + teamId);
        }
    }

    // Helper method to get user by ID
    private User getUserById(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            throw new RuntimeException("User not found with id " + userId);
        }
    }

    // Ensure only OWNER or ADMIN can modify team settings
    // TeamService.java (Modified)
    public void validateOwnerOrAdmin(String userId, String teamId) {
        Team team = getTeamById(teamId);
        User user = getUserById(userId);

        Roles userRole = user.getRoles();
        if (!(userRole == Roles.OWNER || userRole == Roles.ADMIN)) {
            throw new RuntimeException("Only OWNER or ADMIN can perform this action");
        }
    }

    public void validateOwner(String userId, String teamId) {
        Team team = getTeamById(teamId);
        User user = getUserById(userId);

        Roles userRole = user.getRoles();
        if (userRole != Roles.OWNER) {
            throw new RuntimeException("Only OWNER can perform this action");
        }
    }

    public void validateMember(String userId, String teamId) {
        Team team = getTeamById(teamId);
        User user = getUserById(userId);

        if (!team.getMembers().contains(user)) {
            throw new RuntimeException("User is not a member of this team");
        }
    }

}
