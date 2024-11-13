package simpleenergy.task.TaskAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import simpleenergy.task.TaskAPI.model.User;
import simpleenergy.task.TaskAPI.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user != null && user.get().getPassword().equals(password)) {
            return user;
        }
        return null; // Invalid credentials
    }

    public User signUp(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already taken");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword (passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}