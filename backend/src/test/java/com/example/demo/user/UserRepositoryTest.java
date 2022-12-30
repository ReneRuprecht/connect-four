package com.example.demo.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
@AutoConfigureTestDatabase
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldGetUserByName() {

        // given
        String name = "Muster";

        User user = new User("Muster", "test@test.com", "123");

        userRepository.save(user);

        // when
        User expected = userRepository.getUserByName(name).get();

        // then
        assertThat(expected).isNotNull();
    }

    @Test
    void shouldNotGetUserByName() {

        // given
        String name = "Muster";

        // when
        boolean expected = userRepository.getUserByName(name).isPresent();

        // then
        assertThat(expected).isFalse();
    }

    @Test
    void shouldGetUserByEmail() {

        // given
        String email = "test@test.com";

        User user = new User("Muster", "test@test.com", "123");

        userRepository.save(user);

        // when
        User expected = userRepository.getUserByEmail(email).get();

        // then
        assertThat(expected).isNotNull();
    }

    @Test
    void shouldNotGetUserByEmail() {

        // given
        String email = "test@test.com";

        // when
        boolean expected = userRepository.getUserByEmail(email).isPresent();

        // then
        assertThat(expected).isFalse();
    }
}
