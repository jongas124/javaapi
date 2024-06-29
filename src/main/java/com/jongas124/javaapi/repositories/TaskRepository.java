package com.jongas124.javaapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import com.jongas124.javaapi.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @Query(value = "SELECT * FROM tasks WHERE user_id = :userId", nativeQuery = true)
    List<Task> findByUserId(@Param("userId") Long userId);

}
