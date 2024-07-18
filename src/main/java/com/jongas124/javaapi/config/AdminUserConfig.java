package com.jongas124.javaapi.config;

import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.jongas124.javaapi.models.Profile;
import com.jongas124.javaapi.models.User;
import com.jongas124.javaapi.repositories.ProfileRepository;
import com.jongas124.javaapi.repositories.UserRepository;
import com.jongas124.javaapi.util.Argon2Encoder;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class AdminUserConfig implements CommandLineRunner{

    private ProfileRepository profileRepository;
    private UserRepository userRepository;
    private Argon2Encoder argon2Encoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Profile profile = profileRepository.findByProfile(Profile.ProfileEnum.ADMIN.name());

        Optional<User> userAdmin = userRepository.findByUsername("admin");

        if(userAdmin.isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(argon2Encoder.encode("123"));
            user.setProfiles(Set.of(profile));
            userRepository.save(user);
        } else {
            System.out.println("Admin já existe");
        }
    }


}
