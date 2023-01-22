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
import com.example.demo.user.exception.EmailAlreadyExistsException;
import com.example.demo.user.exception.UsernameAlreadyExistsException;
import com.example.demo.user.exception.UserNameNotFoundException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService underTest;

    User USER_RECORD_1;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
        USER_RECORD_1 = User.builder()
            .name("Muster")
            .email("test@email.com")
            .password("123")
            .role(Role.USER)
            .build();
    }

    @Test
    void shouldCreateUser() {

        // when
        underTest.createUser(USER_RECORD_1);
        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(USER_RECORD_1);

    }

    @Test
    void shouldNotCreateUserDuplicateEntry() {

        // given
        String testEmail = USER_RECORD_1.getEmail();

        // when
        Mockito.when(userRepository.getUserByEmail(testEmail))
            .thenReturn(Optional.of(USER_RECORD_1));

        EmailAlreadyExistsException actual = assertThrows(EmailAlreadyExistsException.class,
                () -> underTest.createUser(USER_RECORD_1));

        // then
        assertEquals(String.format("Email %s already exists", testEmail), actual.getMessage());

    }

    @Test
    void shouldNotCreateUserUsernameAlreadyExists() {

        // given
        String testName = USER_RECORD_1.getName();

        // when
        Mockito.when(userRepository.getUserByName(testName))
            .thenReturn(Optional.of(USER_RECORD_1));

        UsernameAlreadyExistsException actual = assertThrows(UsernameAlreadyExistsException.class,
                () -> underTest.createUser(USER_RECORD_1));

        // then
        assertEquals(String.format("Username %s already exists", testName), actual.getMessage());

    }

    @Test
    void shouldDeleteUserByName() {

        // when
        Mockito.when(userRepository.getUserByName(USER_RECORD_1.getName()))
            .thenReturn(Optional.of(USER_RECORD_1));

        underTest.deleteUserByName(USER_RECORD_1.getName());

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).delete(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(USER_RECORD_1);
    }

    @Test
    void shouldNotDeleteUserByNameButThrowUserNameNotFoundExceptopn() {

        // when
        UserNameNotFoundException actual = assertThrows(UserNameNotFoundException.class,
                () -> underTest.deleteUserByName(USER_RECORD_1.getName()));

        // then
        assertEquals(String.format("Username %s not found", USER_RECORD_1.getName()), actual.getMessage());
    }

    @Test
    void shouldGetUserByEmail() {

        // when
        Mockito.when(userRepository.getUserByEmail(USER_RECORD_1.getName()))
            .thenReturn(Optional.of(USER_RECORD_1));

        User actual = underTest.getUserByEmail(USER_RECORD_1.getName());

        // then
        assertThat(actual).isEqualTo(USER_RECORD_1);
    }

    @Test
    void shouldGetUserByName() {
        // when
        Mockito.when(userRepository.getUserByName(USER_RECORD_1.getName()))
            .thenReturn(Optional.of(USER_RECORD_1));

        User actual = underTest.getUserByName(USER_RECORD_1.getName());

        // then
        assertThat(actual).isEqualTo(USER_RECORD_1);
    }
}
