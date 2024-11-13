package simpleenergy.task.TaskAPI.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleenergy.task.TaskAPI.model.Team;
import simpleenergy.task.TaskAPI.enums.Roles;
import simpleenergy.task.TaskAPI.service.TeamService;
import simpleenergy.task.TaskAPI.util.JwtUtil;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    // Create a team (only available to authenticated users with OWNER role)
    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestParam String teamName) {
        Team team = teamService.createTeam(teamName);
        return ResponseEntity.ok(team);
    }

    // Add a user to the team (only available to owner or admin)
    @PostMapping("/{teamId}/add-user")
    public ResponseEntity<Team> addUserToTeam(@PathVariable String teamId, @RequestParam String userId, @RequestParam Roles role) {
        // Extract JWT token from Authorization header
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove "Bearer " prefix
            String authUserId = jwtUtil.extractUserId(token);
            String authRole = jwtUtil.extractRole(token);

            // Validate user role
            teamService.validateOwnerOrAdmin(authUserId, teamId);

            Team team = teamService.addUserToTeam(teamId, userId, role);
            return ResponseEntity.ok(team);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    // Remove a user from the team (only available to owner)
    @DeleteMapping("/{teamId}/remove-user")
    public ResponseEntity<Team> removeUserFromTeam(@PathVariable String teamId, @RequestParam String userId) {
        // Validate that the user has OWNER role
        teamService.validateOwner(userId, teamId);

        Team team = teamService.removeUserFromTeam(teamId, userId);
        return ResponseEntity.ok(team);
    }

    // Assign a role to a user in the team (only available to owner)
    @PostMapping("/{teamId}/assign-role")
    public ResponseEntity<Team> assignRoleToUser(@PathVariable String teamId, @RequestParam String userId, @RequestParam Roles role) {
        // Validate that the user has OWNER role
        teamService.validateOwner(userId, teamId);

        Team team = teamService.assignRoleToUser(teamId, userId, role);
        return ResponseEntity.ok(team);
    }

    // Get users in a team (ensure that the user is a member before fetching team details)
    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getUsersInTeam(@PathVariable String teamId, @RequestParam String userId) {
        // Validate that the user is a member of the team
        teamService.validateMember(userId, teamId);

        Team team = teamService.getUsersInTeam(teamId);
        return ResponseEntity.ok(team);
    }
}
