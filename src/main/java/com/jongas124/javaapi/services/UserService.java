package com.jongas124.javaapi.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jongas124.javaapi.controllers.dto.LoginRequest;
import com.jongas124.javaapi.models.Profile;
import com.jongas124.javaapi.models.User;
import com.jongas124.javaapi.repositories.ProfileRepository;
import com.jongas124.javaapi.repositories.UserRepository;
import com.jongas124.javaapi.services.exceptions.DataBindingViolationException;
import com.jongas124.javaapi.services.exceptions.InvalidCredentialsException;
import com.jongas124.javaapi.services.exceptions.ObjectNotFoundException;
import com.jongas124.javaapi.services.exceptions.PermissionException;
import com.jongas124.javaapi.util.Argon2Encoder;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private Argon2Encoder argon2Encoder = new Argon2Encoder();

    @Transactional
    public User create(User obj) {
        Profile profile = profileRepository.findByProfile(Profile.ProfileEnum.USER.name());
        obj.setId(null);
        obj.setPassword(argon2Encoder.encode(obj.getPassword()));
        obj.setProfiles(Set.of(profile));
        obj = this.userRepository.save(obj);
        return obj;
    }

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new ObjectNotFoundException("Usuário não encontrado Id: " + id);
        }
    }

    public User findByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new InvalidCredentialsException("Usuário ou senha incorreto(s)");
        } else {
            return user.get();
        }

    }

    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Transactional
    public User update(User obj) {
        User newObj = this.findById(obj.getId());
        newObj.setPassword(argon2Encoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id, JwtAuthenticationToken jwtAuthenticationToken) {
        User user = this.findById(Long.parseLong(jwtAuthenticationToken.getName()));
        if(id.equals(Long.parseLong(jwtAuthenticationToken.getName())) || isAdmin(user)) {
            try {
                this.userRepository.deleteById(id);
            } catch (Exception e) {
                throw new DataBindingViolationException("Não é possível excluir excluir pois há entidades (tasks) relacioandas Id: " + id);
            }
        } else {
            throw new PermissionException("Não é possível apagar a conta de outros usuários");
        }
    }

    public boolean isLoginCorrect(LoginRequest loginRequest, User user) {
        Argon2Encoder argon2Encoder = new Argon2Encoder();
        if (argon2Encoder.matches(loginRequest.password(), user.getPassword())) {
            return true;
        } else {
            throw new InvalidCredentialsException("Usuário ou senha incorreto(s)");
        }
    }

    public boolean isAdmin(User user) {
        return user.getProfiles().stream()
        .anyMatch(profile -> profile.getProfile().equalsIgnoreCase(Profile.ProfileEnum.ADMIN.name()));
    }

}
