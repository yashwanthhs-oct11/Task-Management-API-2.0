package simpleenergy.task.TaskAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import simpleenergy.task.TaskAPI.model.Task;
import simpleenergy.task.TaskAPI.model.User;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(cron = "0 0 * * * *")  // Every hour
    public void checkForOverdueTasks() {
        List<Task> overdueTasks = taskService.getOverdueTasks();
        for (Task task : overdueTasks) {
            notifyTeamMembers(task);
        }
    }

    public void notifyTeamMembers(Task task) {
        for (User member : task.getTeam().getMembers()) {
            sendEmailNotification(member.getEmail(), "Task Overdue",
                    "The task " + task.getTitle() + " is overdue. Please check it.");
        }
    }

    private void sendEmailNotification(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

