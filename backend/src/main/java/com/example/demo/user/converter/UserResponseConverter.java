package com.example.demo.user.converter;

import org.springframework.stereotype.Service;
import com.example.demo.user.User;
import com.example.demo.user.response.UserResponse;

@Service
public class UserResponseConverter {
    public UserResponse convertToUserResponseFromUser(User user) {

        return UserResponse.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
                .build();
    }
}
