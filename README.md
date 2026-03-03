# Todo API — v1.0 (MC62)

A secure **REST API** built with **Spring Boot** for managing personal tasks.

This API allows authenticated users to **create, read, update, delete, and search**
their own tasks, following clean REST principles and proper security practices.

Each user can only access their own tasks.

This project runs on **Java 8+** and is part of a collection of small projects made for fun and learning.  
https://roadmap.sh/projects/todo-list-api

---

## Features

- User authentication (Spring Security)
- Create tasks
- Update existing tasks
- Delete tasks
- Retrieve a task by ID (owner only)
- List tasks with pagination
- Search tasks by title (case-insensitive)
- Ownership validation (users cannot access others' tasks)
- Proper HTTP status codes
- Service-level and controller-level tests (JUnit + Mockito + MockMvc)

Returned data includes:
- id
- title
- description

---

## Security

- All endpoints require authentication
- Users can only access their own tasks
- Accessing another user's task returns **403 FORBIDDEN**
- Accessing a non-existing task returns **404 NOT FOUND**

---

## API Endpoints

### Create Task

POST /todos

```json
{
  "title": "Learn Spring Boot",
  "description": "Study security and testing"
}
```

Returns 201 CREATED

---

### Get Task By ID

GET /todos/{id}

Returns:
- 200 OK
- 404 NOT FOUND (if task does not exist for user)
- 403 FORBIDDEN (if trying to access another user's task)

---

### Update Task

PUT /todos/{id}

```json
{
  "title": "Updated Title",
  "description": "Updated description"
}
```

Returns:
- 200 OK
- 404 NOT FOUND
- 403 FORBIDDEN

---

### Delete Task

DELETE /todos/{id}

Returns:
- 204 NO CONTENT
- 404 NOT FOUND
- 403 FORBIDDEN

---

### List User Tasks (Paginated)

GET /todos?page=1&limit=10

Returns:

```json
{
  "data": [
    {
      "id": 1,
      "title": "Task title",
      "description": "Task description"
    }
  ],
  "page": 1,
  "limit": 10,
  "total": 5
}
```

---

### Search Tasks by Title

GET /todos?page=1&limit=10&title=spring

Performs case-insensitive search on title.

---

## Validation

Title is required  
Description is required  
Invalid requests return 400 BAD REQUEST

---

## Tech Stack

- Java 8+
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Maven
- JUnit 5
- Mockito
- MockMvc
- H2 / SQL Database

---


## Testing

- Unit tests for service layer
- Controller tests with MockMvc
- Security tests (401 / 403 scenarios)
- Pagination and filtering tests

---

## Status

✔ Core CRUD operations implemented  
✔ JWT authentication implemented  
✔ Access & Refresh token mechanism  
✔ Ownership validation enforced  
✔ Pagination, filtering & sorting implemented  
✔ Unit & controller tests implemented

Bonus features completed:
- Filtering and sorting for the to-do list
- Unit tests for the API
- Refresh token mechanism for authentication

Planned future improvements:
- Rate limiting & throttling
- Dockerization
- CI/CD pipeline