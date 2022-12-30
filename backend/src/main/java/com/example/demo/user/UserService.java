package com.example.demo.user;

import org.springframework.stereotype.Service;
import com.example.demo.user.exception.UserEmailNotFoundException;
import com.example.demo.user.exception.UserNameNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserByName(String name) {
        User user = userRepository.getUserByName(name)
                .orElseThrow(() -> new UserNameNotFoundException(name));

        userRepository.delete(user);
    }

    public User getUserByName(String name) {
        return userRepository.getUserByName(name)
                .orElseThrow(() -> new UserNameNotFoundException(name));
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));
    }



}
