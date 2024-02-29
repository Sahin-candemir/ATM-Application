package com.patika.Atm.service;

import com.patika.Atm.dto.CreateUserRequest;
import com.patika.Atm.dto.UserDto;
import com.patika.Atm.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserRequest createUserRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    void deleteUserById(Long id);
}
