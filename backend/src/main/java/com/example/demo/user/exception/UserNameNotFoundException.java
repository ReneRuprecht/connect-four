package com.example.demo.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.example.demo.user.config.Constants;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String name) {
        super(String.format(Constants.USERNAME_NOT_FOUND_MESSAGE, name));
    }
}
