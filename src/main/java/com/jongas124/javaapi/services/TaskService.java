package com.jongas124.javaapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jongas124.javaapi.models.Task;
import com.jongas124.javaapi.models.User;
import com.jongas124.javaapi.repositories.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        if(task.isPresent()) {
            return task.get();
        } else{
        throw new RuntimeException("Task não encontrada!");
        }
    }

    public List<Task> findAllByUserId(Long userId) {
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
       Task newObj =  this.findById(obj.getId());
       newObj.setDescricao(obj.getDescricao());
       return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        this.findById(id);
        this.taskRepository.deleteById(id);
    }
}
