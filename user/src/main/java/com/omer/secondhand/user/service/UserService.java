package com.omer.secondhand.user.service;

import com.omer.secondhand.user.dto.*;
import com.omer.secondhand.user.exception.UserIsNotActiveException;
import com.omer.secondhand.user.exception.UserNotFoundException;
import com.omer.secondhand.user.mapper.UserMapper;
import com.omer.secondhand.user.model.User;
import com.omer.secondhand.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userMapper.convertUserListToUserDTOList(userRepository.findAll());
    }

    public UserDTO getUserByMail(String mail) {
        User user = findByMail(mail);
        return userMapper.convertUserToUserDTO(user);
    }

    public UserDTO createUser(CreateUserRequest userRequest) {
        User user = userMapper.createUserRequestToUser(userRequest);
        user.setIsActive(false);
        return userMapper.convertUserToUserDTO(userRepository.save(user));
    }

    public UserDTO updateUser(String mail, UpdateUserRequest updateUserRequest) {
        User user = findByMail(mail);
        if (!user.getIsActive()) {
            log.warn(String.format("User with mail %s is not active", mail));
            throw new UserIsNotActiveException();
        }
        user = userMapper.updateUserRequestToUser(updateUserRequest, user);
        return userMapper.convertUserToUserDTO(userRepository.save(user));
    }

    public void deactivateUser(Long id) {
        changeActiveUser(id, false);
    }

    public void activeUser(Long id) {
        changeActiveUser(id, true);
    }

    public void deleteUser(Long id) {
        findById(id);

        userRepository.deleteById(id);
    }


    private void changeActiveUser(Long id, boolean isActive) {
        User user = findById(id);
        user.setIsActive(isActive);
        userRepository.save(user);
    }

    private User findByMail(String mail) {
        return userRepository.findByMail(mail).orElseThrow(() -> new UserNotFoundException("User couldn't be found by following mail: " + mail));
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User couldn't be found by following id: " + id));
    }


}