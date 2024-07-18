package com.jongas124.javaapi.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends BadCredentialsException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
