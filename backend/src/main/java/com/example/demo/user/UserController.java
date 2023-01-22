package com.example.demo.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.user.request.UserCreateRequest;
import com.example.demo.user.response.UserResponse;
import com.example.demo.user.request.GetUserByEmailRequest;
import com.example.demo.user.request.GetUserByNameRequest;
import lombok.AllArgsConstructor;
import com.example.demo.user.config.Constants;
import com.example.demo.user.converter.UserResponseConverter;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private UserService userService;
    private UserResponseConverter userResponseConverter;

    @PostMapping(Constants.USER_GET_BY_NAME_URL)
    public ResponseEntity<UserResponse> getUserByName(@RequestBody GetUserByNameRequest getUserByNameRequest) {

        User user = userService.getUserByName(getUserByNameRequest.getName());

        return ResponseEntity.ok()
            .body(userResponseConverter.convertToUserResponseFromUser(user));
    }

    @PostMapping(Constants.USER_GET_BY_EMAIL_URL)
    public ResponseEntity<UserResponse> getUserByEmail(@RequestBody GetUserByEmailRequest getUserByEmailRequest) {

        User user = userService.getUserByEmail(getUserByEmailRequest.getEmail());

        return ResponseEntity.ok()
            .body(userResponseConverter.convertToUserResponseFromUser(user));
    }

}
