package com.example.demo.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.security.jwt.JwtAuthenticationFilter;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.user.config.Constants;
import com.example.demo.user.converter.UserResponseConverter;
import com.example.demo.user.exception.EmailNotFoundException;
import com.example.demo.user.exception.UserNameNotFoundException;
import com.example.demo.user.request.GetUserByEmailRequest;
import com.example.demo.user.request.GetUserByNameRequest;
import com.example.demo.user.request.UserCreateRequest;
import com.example.demo.user.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

        @Autowired
        MockMvc mockMvc;

        @Mock
        UserRepository userRepository;

        @MockBean
        UserService userService;

        JwtService jwtService;

        @MockBean
        JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        UserResponseConverter userResponseConverter;

        @Autowired
        ObjectMapper objectMapper;

        User USER_RECORD_1 = new User("Muster", "test@email.com", "123", Role.USER);
        String URL_PREFIX = "/api/v1/users";

        @BeforeEach
        void setUp() {
                jwtService = new JwtService();

        }

        @Test
        void shouldCreateUser() throws Exception {
                // given
                UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                        .user(USER_RECORD_1)
                        .build();

                String token = jwtService.generateToken(USER_RECORD_1);

                ResultActions result = mockMvc
                        .perform(post(URL_PREFIX + Constants.USER_CREATE_URL).contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token)
                                .content(objectMapper.writeValueAsString(userCreateRequest)));

                result.andExpect(MockMvcResultMatchers.content()
                        .string(USER_RECORD_1.getEmail()))
                        .andExpect(status().isCreated());

        }

        @Test

        void shouldGetUserByEmail() throws Exception {
                GetUserByEmailRequest getUserByEmailRequest = new GetUserByEmailRequest();
                getUserByEmailRequest.setEmail(USER_RECORD_1.getEmail());

                UserResponse userResponse = UserResponse.builder()
                        .id(USER_RECORD_1.getId())
                        .name(USER_RECORD_1.getName())
                        .email(USER_RECORD_1.getEmail())
                        .build();

                String token = jwtService.generateToken(USER_RECORD_1);

                when(userService.getUserByEmail(getUserByEmailRequest.getEmail())).thenReturn(USER_RECORD_1);

                when(userResponseConverter.convertToUserResponseFromUser(USER_RECORD_1)).thenReturn(userResponse);

                ResultActions result = mockMvc.perform(post(URL_PREFIX + Constants.USER_GET_BY_EMAIL_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(getUserByEmailRequest)));

                verify(userService).getUserByEmail(getUserByEmailRequest.getEmail());

                result.andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                .exists())
                        .andExpect(status().isOk());
        }

        @Test
        void shouldNotGetUserByEmailBecauseEmailDoesNotExist() throws JsonProcessingException, Exception {
                GetUserByEmailRequest getUserByEmailRequest = new GetUserByEmailRequest();
                getUserByEmailRequest.setEmail(USER_RECORD_1.getEmail());

                when(userService.getUserByEmail(getUserByEmailRequest.getEmail()))
                        .thenThrow(new EmailNotFoundException(USER_RECORD_1.getEmail()));

                ResultActions result = mockMvc.perform(post(URL_PREFIX + Constants.USER_GET_BY_EMAIL_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUserByEmailRequest)));

                verify(userService).getUserByEmail(getUserByEmailRequest.getEmail());

                result.andExpect(status().is4xxClientError())
                        .andExpect(e -> assertTrue(e.getResolvedException() instanceof EmailNotFoundException))
                        .andExpect(mvcResult -> assertEquals(
                                        String.format(Constants.EMAIL_NOT_FOUND_MESSAGE, USER_RECORD_1.getEmail()),
                                        mvcResult.getResolvedException()
                                                .getMessage()));
        }

        @Test
        void shouldGetUserByName() throws JsonProcessingException, Exception {
                GetUserByNameRequest getUserByNameRequest = new GetUserByNameRequest();
                getUserByNameRequest.setName(USER_RECORD_1.getName());

                UserResponse userResponse = UserResponse.builder()
                        .id(USER_RECORD_1.getId())
                        .name(USER_RECORD_1.getName())
                        .email(USER_RECORD_1.getEmail())
                        .build();

                when(userService.getUserByName(getUserByNameRequest.getName())).thenReturn(USER_RECORD_1);

                when(userResponseConverter.convertToUserResponseFromUser(USER_RECORD_1)).thenReturn(userResponse);

                ResultActions result = mockMvc.perform(post(URL_PREFIX + Constants.USER_GET_BY_NAME_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUserByNameRequest)));

                verify(userService).getUserByName(getUserByNameRequest.getName());

                result.andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                .exists())
                        .andExpect(status().isOk());
        }

        @Test
        void shouldNotGetUserByNameBecauseUsernameDoesNotExist() throws JsonProcessingException, Exception {
                GetUserByNameRequest getUserByNameRequest = new GetUserByNameRequest();
                getUserByNameRequest.setName(USER_RECORD_1.getName());

                when(userService.getUserByName(getUserByNameRequest.getName()))
                        .thenThrow(new UserNameNotFoundException(getUserByNameRequest.getName()));

                ResultActions result = mockMvc.perform(post(URL_PREFIX + Constants.USER_GET_BY_NAME_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUserByNameRequest)));

                verify(userService).getUserByName(getUserByNameRequest.getName());

                result.andExpect(status().is4xxClientError())
                        .andExpect(mvcResult -> assertTrue(
                                        mvcResult.getResolvedException() instanceof UserNameNotFoundException))
                        .andExpect(mvcResult -> assertEquals(
                                        String.format(Constants.USERNAME_NOT_FOUND_MESSAGE,
                                                        getUserByNameRequest.getName()),
                                        mvcResult.getResolvedException()
                                                .getMessage()));
        }

}
