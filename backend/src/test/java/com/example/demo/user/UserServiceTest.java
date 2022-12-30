package com.example.demo.user;

import static org.mockito.Mockito.verify;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.user.exception.UserNameNotFoundException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService underTest;



    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);

    }

    @Test
    void shouldCreateUser() {
        // given
        User user = new User("Muster", "test@test.com", "123");
        // when
        underTest.createUser(user);
        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();


        assertThat(capturedUser).isEqualTo(user);

    }

    @Test
    void shouldDeleteUserByName() {
        // given
        User USER_RECORD_1 = new User("Muster", "test@test.com", "123");

        Mockito.when(userRepository.getUserByName(USER_RECORD_1.getName()))
                .thenReturn(Optional.of(USER_RECORD_1));

        // when
        underTest.deleteUserByName(USER_RECORD_1.getName());

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).delete(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();


        assertThat(capturedUser).isEqualTo(USER_RECORD_1);
    }

    @Test
    void shouldNotDeleteUserByNameButThrowUserNameNotFoundExceptopn() {
        // given
        User USER_RECORD_1 = new User("Muster", "test@test.com", "123");


        assertThrows(UserNameNotFoundException.class,
                () -> underTest.deleteUserByName(USER_RECORD_1.getName()));
    }



    @Test
    void shouldGetUserByEmail() {
        // given
        User USER_RECORD_1 = new User("Muster", "test@test.com", "123");

        // when
        Mockito.when(userRepository.getUserByEmail(USER_RECORD_1.getName()))
                .thenReturn(Optional.of(USER_RECORD_1));

        User expected = underTest.getUserByEmail(USER_RECORD_1.getName());

        // then
        assertThat(expected).isEqualTo(USER_RECORD_1);
    }

    @Test
    void shouldGetUserByName() {
        // given
        User USER_RECORD_1 = new User("Muster", "test@test.com", "123");

        // when
        Mockito.when(userRepository.getUserByName(USER_RECORD_1.getName()))
                .thenReturn(Optional.of(USER_RECORD_1));

        User expected = underTest.getUserByName(USER_RECORD_1.getName());

        // then
        assertThat(expected).isEqualTo(USER_RECORD_1);
    }
}
