package com.example.demo.user.exception;

public class UserNameNotFoundException extends RuntimeException {

    public UserNameNotFoundException(String name) {
        super(name + " not found");
    }
}
