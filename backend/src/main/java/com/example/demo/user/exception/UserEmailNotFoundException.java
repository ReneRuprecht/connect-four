package com.example.demo.user.exception;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String email) {
        super(email + " not found");
    }
}
