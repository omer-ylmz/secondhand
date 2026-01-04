package com.omer.secondhand.user.service;

import com.omer.secondhand.user.dto.*;
import com.omer.secondhand.user.exception.UserNotFoundException;
import com.omer.secondhand.user.mapper.UserMapper;
import com.omer.secondhand.user.model.User;
import com.omer.secondhand.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;
    private final UserMapper userMapper;


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::convertUserToUserDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(String mail) {
        User user = findByMail(mail);
        return userMapper.convertUserToUserDTO(user);
    }

    public UserDTO createUser(CreateUserRequest userRequest) {
        User user = userMapper.createUserRequestToUser(userRequest);
        return userMapper.convertUserToUserDTO(userRepository.save(user));
    }

    public UserDTO updateUser(String mail, UpdateUserRequest updateUserRequest) {
        User user = findByMail(mail);
        user = userMapper.updateUserRequestToUser(updateUserRequest, user);
        return userMapper.convertUserToUserDTO(userRepository.save(user));
    }

    private User findByMail(String mail) {
        return userRepository.findByMail(mail).orElseThrow(() -> new UserNotFoundException("User couldn't be found by following id: " + mail));
    }


}
