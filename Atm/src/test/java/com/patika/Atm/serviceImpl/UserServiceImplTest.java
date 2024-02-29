package com.patika.Atm.serviceImpl;

import com.patika.Atm.dto.CreateUserRequest;
import com.patika.Atm.dto.UserDto;
import com.patika.Atm.model.Role;
import com.patika.Atm.model.User;
import com.patika.Atm.repository.UserRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser() {
        User user = User.builder()
                .id(1000L)
                .email("shncandemir@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .password(passwordEncoder.encode("123456"))
                .role(Role.USER)
                .build();

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email("shncandemir@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .password("123456")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1000L)
                .email("shncandemir@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .build();

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDto savedUser = userService.createUser(createUserRequest);

        assertNull(savedUser);
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void deleteUserById() {
    }

    @Test
    void getByUsername() {
    }
}