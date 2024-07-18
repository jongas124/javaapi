package com.jongas124.javaapi.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jongas124.javaapi.controllers.dto.LoginRequest;
import com.jongas124.javaapi.controllers.dto.LoginResponse;
import com.jongas124.javaapi.models.Profile;
import com.jongas124.javaapi.models.User;
import com.jongas124.javaapi.services.UserService;

import lombok.AllArgsConstructor;

@RestController
@Validated
@RequestMapping("/login")
@AllArgsConstructor
public class TokenController {
    private final JwtEncoder jwtEncoder;

    private UserService userService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = this.userService.findByUsername(loginRequest.username());
        userService.isLoginCorrect(loginRequest, user);

        Instant now = Instant.now();
        Long expiresIn = 3000L;
        
        String scope = user.getProfiles()
                .stream()
                .map(Profile::getProfile)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("javaapi")
            .subject(user.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .claim("scope", scope)
            .build();

        String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
