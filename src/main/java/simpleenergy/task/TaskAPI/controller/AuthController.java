package simpleenergy.task.TaskAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simpleenergy.task.TaskAPI.model.User;
import simpleenergy.task.TaskAPI.service.UserService;
import simpleenergy.task.TaskAPI.util.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        // Authenticate the user
        Optional<User> user = userService.authenticate(username, password);
        if (user.isPresent()) {
            // Generate JWT token
            String token = jwtUtil.generateToken(user.get().getId(), String.valueOf(user.get().getRoles()));
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        try {
            User savedUser  = userService.signUp(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}