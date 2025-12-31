package com.omer.secondhand.user.service;

import com.omer.secondhand.user.dto.CreateUserRequest;
import com.omer.secondhand.user.dto.UpdateUserRequest;
import com.omer.secondhand.user.dto.UserDTO;
import com.omer.secondhand.user.dto.UserDtoConverter;
import com.omer.secondhand.user.exception.UserNotFoundException;
import com.omer.secondhand.user.model.User;
import com.omer.secondhand.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;

    public UserService(UserRepository userRepository, UserDtoConverter userDtoConverter) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userDtoConverter::convert).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = findById(id);
        return userDtoConverter.convert(user);
    }

    public UserDTO createUser(CreateUserRequest userRequest) {
        User user = new User(null,
                userRequest.getMail(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getMiddleName());
        return userDtoConverter.convert(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = findById(id);
        User updatedUser = new User(user.getId(),
                user.getMail(),
                updateUserRequest.getFirstName(),
                updateUserRequest.getLastName(),
                updateUserRequest.getMiddleName());
        return userDtoConverter.convert(userRepository.save(updatedUser));
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User couldn't be found by following id: " + id));
    }


}
