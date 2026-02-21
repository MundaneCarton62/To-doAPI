package com.example.To_doAPI.repository;

import com.example.To_doAPI.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findByUserIdAndTitleContainingIgnoreCase(
            Long userId,
            String title,
            Pageable pageable);

    Optional<Task> findByIdAndUserId(Long id, Long userId);
}
