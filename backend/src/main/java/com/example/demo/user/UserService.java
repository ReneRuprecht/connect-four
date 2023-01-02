package com.example.demo.user;

import org.springframework.stereotype.Service;
import com.example.demo.user.exception.EmailAlreadyExists;
import com.example.demo.user.exception.EmailNotFoundException;
import com.example.demo.user.exception.UsernameAlreadyInUseException;
import com.example.demo.user.exception.UsernameNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;


    public void createUser(User user) {
        userRepository.getUserByEmail(user.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExists(u.getEmail());
        });
        userRepository.getUserByName(user.getName()).ifPresent(u -> {
            throw new UsernameAlreadyInUseException(u.getName());
        });

        userRepository.save(user);
    }

    public void deleteUserByName(String name) {
        User user = userRepository.getUserByName(name)
                .orElseThrow(() -> new UsernameNotFoundException(name));

        userRepository.delete(user);
    }

    public User getUserByName(String name) {

        return userRepository.getUserByName(name)
                .orElseThrow(() -> new UsernameNotFoundException(name));
    }

    public User getUserByEmail(String email) {

        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

    }

}
