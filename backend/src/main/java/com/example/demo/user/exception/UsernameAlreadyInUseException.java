package com.example.demo.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.example.demo.user.config.Constants;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyInUseException extends RuntimeException {

    public UsernameAlreadyInUseException(String name) {
        super(String.format(Constants.USERNAME_ALREADY_EXISTS, name));
    }

}
