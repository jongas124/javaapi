package com.jongas124.javaapi.services;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jongas124.javaapi.models.User;
import com.jongas124.javaapi.models.enums.ProfileEnum;
import com.jongas124.javaapi.repositories.UserRepository;
import com.jongas124.javaapi.services.exceptions.DataBindingViolationException;
import com.jongas124.javaapi.services.exceptions.ObjectNotFoundException;


@Service
public class UserService {

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User create(User obj) {
        obj.setId(null);
        obj.setPassword(argon2PasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Collections.singleton(ProfileEnum.USER.getCode()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new ObjectNotFoundException("User not found! Id: " + id);
        }
    }

    @Transactional
    public User update(User obj) {
        User newObj = this.findById(obj.getId());
        newObj.setPassword(argon2PasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        this.findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir excluir pois há entidades (tasks) relacioandas Id: " + id);
        }
    }

}
