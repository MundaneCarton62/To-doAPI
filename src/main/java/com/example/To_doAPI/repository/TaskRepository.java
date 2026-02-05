package com.example.To_doAPI.repository;

import com.example.To_doAPI.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
