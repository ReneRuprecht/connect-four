package com.example.demo.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.example.demo.user.config.Constants;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {

        super(String.format(Constants.EMAIL_ALREADY_IN_USE_MESSAGE, email));
    }
}
