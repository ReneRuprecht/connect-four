package com.example.demo.user;

import org.springframework.stereotype.Service;
import com.example.demo.user.exception.EmailAlreadyExistsException;
import com.example.demo.user.exception.EmailNotFoundException;
import com.example.demo.user.exception.UserNameNotFoundException;
import com.example.demo.user.exception.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public void createUser(User user) {
        userRepository.getUserByEmail(user.getEmail())
            .ifPresent(u -> {
                throw new EmailAlreadyExistsException(u.getEmail());
            });

        userRepository.getUserByName(user.getName())
            .ifPresent(u -> {
                throw new UsernameAlreadyExistsException(u.getName());
            });

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
            .orElseThrow(() -> new EmailNotFoundException(email));

    }

}
