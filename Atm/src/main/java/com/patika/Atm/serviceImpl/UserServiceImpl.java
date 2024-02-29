package com.patika.Atm.serviceImpl;

import com.patika.Atm.dto.AccountDto;
import com.patika.Atm.dto.CreateUserRequest;
import com.patika.Atm.dto.UserDto;
import com.patika.Atm.dto.UserResponse;
import com.patika.Atm.exception.ResourceNotFoundException;
import com.patika.Atm.model.Role;
import com.patika.Atm.model.User;
import com.patika.Atm.repository.UserRepository;
import com.patika.Atm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;
    @Override
    public UserDto createUser(CreateUserRequest createUserRequest) {
        User user = mapper.map(createUserRequest, User.class);
        user.setPassword(encoder.encode(createUserRequest.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        return UserDto.builder()
                .email(savedUser.getEmail())
                .firstname(savedUser.getFirstname())
                .id(savedUser.getId())
                .lastname(savedUser.getLastname())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> mapper.map(user, UserResponse.class)).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :"+id));
        List<AccountDto> accountDtoList = user.getAccounts().stream().map(account -> mapper.map(account, AccountDto.class)).collect(Collectors.toList());
        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .accountDtoList(accountDtoList)
                .build();
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found exception."));
        userRepository.delete(user);
    }

    public Optional<User> getByUsername(String email){
        return userRepository.findByEmail(email);
    }
}
