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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.security.jwt.JwtAuthenticationFilter;
import com.example.demo.user.config.Constants;
import com.example.demo.user.converter.UserResponseConverter;
import com.example.demo.user.request.GetUserByEmailRequest;
import com.example.demo.user.request.GetUserByNameRequest;
import com.example.demo.user.response.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

@WebMvcTest(value = UserController.class)
@ExtendWith(MockitoExtension.class)

public class UserControllerTest {

        MockMvc mockMvc;

        @Autowired
        private WebApplicationContext context;

        @Mock
        UserRepository userRepository;

        @MockBean
        UserService userService;

        @MockBean
        JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        UserDetailsService userDetailsService;

        @MockBean
        UserResponseConverter userResponseConverter;

        @Autowired
        ObjectMapper objectMapper;

        User USER_RECORD_1;

        String URL_PREFIX = "/api/v1/users";

        @BeforeEach
        void setUp() {

                this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                        .apply(springSecurity())
                        .build();

                USER_RECORD_1 = User.builder()
                        .name("Muster")
                        .email("test@email.com")
                        .password("123")
                        .role(Role.USER)
                        .build();

        }

        @Test
        @WithMockUser()
        void shouldGetUserByEmail() throws Exception {
                GetUserByEmailRequest getUserByEmailRequest = new GetUserByEmailRequest();
                getUserByEmailRequest.setEmail(USER_RECORD_1.getEmail());

                UserResponse userResponse = UserResponse.builder()
                        .id(USER_RECORD_1.getId())
                        .name(USER_RECORD_1.getName())
                        .email(USER_RECORD_1.getEmail())
                        .build();

                when(userService.getUserByEmail(getUserByEmailRequest.getEmail())).thenReturn(USER_RECORD_1);

                when(userResponseConverter.convertToUserResponseFromUser(USER_RECORD_1)).thenReturn(userResponse);

                ResultActions result = mockMvc.perform(post(URL_PREFIX + Constants.USER_GET_BY_EMAIL_URL).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUserByEmailRequest)));

                verify(userService).getUserByEmail(getUserByEmailRequest.getEmail());

                result.andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                                .exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                .exists());

        }

        @Test
        @WithAnonymousUser
        void shouldNotGetUserByEmailBecauseOfUnauthorizedRequest() throws JsonProcessingException, Exception {
                GetUserByEmailRequest getUserByEmailRequest = new GetUserByEmailRequest();
                getUserByEmailRequest.setEmail(USER_RECORD_1.getEmail());

                ResultActions result = mockMvc.perform(post(URL_PREFIX + Constants.USER_GET_BY_EMAIL_URL).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUserByEmailRequest)));

                result.andExpect(status().is(401));

        }

        @Test
        @WithMockUser
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

                ResultActions result = mockMvc.perform(post(URL_PREFIX + Constants.USER_GET_BY_NAME_URL).with(csrf())
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
        @WithAnonymousUser
        void shouldNotGetUserByNameBecauseOfUnauthorizedRequest() throws JsonProcessingException, Exception {
                GetUserByNameRequest getUserByNameRequest = new GetUserByNameRequest();
                getUserByNameRequest.setName(USER_RECORD_1.getName());

                ResultActions result = mockMvc.perform(post(URL_PREFIX + Constants.USER_GET_BY_NAME_URL).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUserByNameRequest)));

                result.andExpect(status().is(401));
        }

}
