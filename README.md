# Task Management API - 2.0

The Task Management API is a RESTful service built using Spring Boot that helps teams manage tasks, track their statuses, and handle team collaboration with email notifications. The application allows users to create tasks, assign them to specific users, track task completion, and manage dependencies between tasks.

## Features

- **User Authentication**: Secure JWT-based authentication for user login.
- **Task Management**: CRUD operations to create, read, update, and delete tasks.
- **Task Filtering and Sorting**: Filter tasks by priority, status, due date, and assigned user. Sort tasks by priority, due date, or creation date.
- **Task Notifications**: Scheduled notifications for overdue tasks, sent to relevant team members.
- **Role-Based Access Control**: Assign different roles (OWNER, ADMIN, MEMBER) to team members with permissions to modify task details and team settings.
- **Task Dependencies**: Handle task dependencies and status changes based on related tasks' completion.

## Prerequisites

- Java 17 or higher
- Spring Boot 2.6 or higher
- MongoDB (or any relational database of your choice)
- SMTP mail configuration for sending notifications (e.g., Gmail, SendGrid)

## Setup

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yashwanthhs-oct11/Task-Management-API-2.0.git
   cd Task-Management-API-2.0
   ```

2. **Setup the database**:
   Configure your database connection in `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=mongoDB-connection-string
   spring.data.mongodb.database=taskmanager
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=yourEmail@gmail.com
   spring.mail.password=emailPassword
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

3. **Install dependencies**:
   Make sure Maven is installed, then build the project:

   ```bash
   mvn clean install
   ```

4. **Run the application**:
   Run the Spring Boot application:

   ```bash
   mvn spring-boot:run
   ```

5. **Postman Collection** (Optional):
   For testing the API, you can import the provided Postman collection in the `[postman](https://www.postman.com/flight-observer-81089823/workspace/taskapi)` folder.

## Authentication

The API uses JWT for authentication. To obtain a token, send a `POST` request to `/login` with the following body:

```json
{
  "username": "yourusername",
  "password": "yourpassword"
}
```

The response will contain a JWT token, which should be used in the `Authorization` header for subsequent requests:

```bash
Authorization: Bearer <JWT_TOKEN>
```

## API Endpoints

### 1. **Task CRUD Operations**

- **Create Task**: `POST /tasks`
- **Get All Tasks**: `GET /tasks`
- **Get Task by ID**: `GET /tasks/{id}`
- **Update Task**: `PUT /tasks/{id}`
- **Delete Task**: `DELETE /tasks/{id}`

### 2. **Filter and Sort Tasks**

- **Filter by Priority**: `GET /tasks/filter/priority?priority=HIGH`
- **Filter by Status**: `GET /tasks/filter/status?status=INCOMPLETE`
- **Filter by Assigned User**: `GET /tasks/filter/assigned-user?assignedUser=username`
- **Sort by Priority**: `GET /tasks/sort/priority`
- **Sort by Due Date**: `GET /tasks/sort/dueDate`
- **Sort by Creation Date**: `GET /tasks/sort/createdAt`

### 3. **Task Dependencies**

- **Update Task Status**: `PUT /tasks/{id}/status`
  - The request body should contain the new task status (`INCOMPLETE`, `COMPLETE`, etc.), and if the task has dependencies, it will only update if all dependencies are marked as complete.

### 4. **User and Team Management**

- **Create Team**: `POST /teams`
- **Add User to Team**: `POST /teams/{teamId}/members`
- **Remove User from Team**: `DELETE /teams/{teamId}/members/{userId}`
- **Assign Role to User**: `PUT /teams/{teamId}/members/{userId}/role`
- **Get Team Members**: `GET /teams/{teamId}/members`

### 5. **Notifications**

- **Task Overdue Notification**: The system automatically sends email notifications to team members of tasks that are overdue.

## Scheduled Jobs

- **Overdue Task Check**: Every hour, the system checks for overdue tasks and sends email notifications to the assigned users.

## Security

The API is secured using JWT (JSON Web Tokens) for authentication. Users must log in first to get a JWT token, which is then used in the Authorization header for all subsequent requests.

### JwtUtil Class

The `JwtUtil` class handles the creation, validation, and extraction of JWT tokens. It ensures that only valid tokens are accepted for requests.

### JwtAuthFilter Class

The `JwtAuthFilter` class is responsible for filtering incoming requests and verifying the JWT token in the Authorization header.

## Sample Code for Task Creation

```json
{
  "title": "Fix API Bug",
  "description": "Resolve the issue with task creation endpoint",
  "priority": "HIGH",
  "status": "INCOMPLETE",
  "dueDate": "2024-11-16T10:00:00",
  "assignedUser": "userId"
}
```

## Tech Stack

- **Backend**: Spring Boot, Java 17
- **Database**: MongoDB
- **Security**: JWT
- **Email Notifications**: JavaMailSender
- **Task Scheduling**: Spring Scheduling (for notifications)
- **Dependency Management**: Maven

## Future Improvements

- Implement a user dashboard with real-time updates.
- Add support for task comments and attachments.
- Implement a reporting feature for team performance based on task completion.
