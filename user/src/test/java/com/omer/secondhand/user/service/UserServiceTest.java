package com.omer.secondhand.user.service;

import com.omer.secondhand.user.TestSupport;
import com.omer.secondhand.user.dto.CreateUserRequest;
import com.omer.secondhand.user.dto.UpdateUserRequest;
import com.omer.secondhand.user.dto.UserDTO;
import com.omer.secondhand.user.exception.UserIsNotActiveException;
import com.omer.secondhand.user.exception.UserNotFoundException;
import com.omer.secondhand.user.mapper.UserMapper;
import com.omer.secondhand.user.model.User;
import com.omer.secondhand.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends TestSupport {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    public void testGetAllUsers_itShouldReturnUserDTOList() {
        List<User> userList = generateUsers();
        List<UserDTO> userDTOList = generateUserDTOList(userList);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.convertUserListToUserDTOList(userList)).thenReturn(userDTOList);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(userDTOList, result);
        verify(userRepository).findAll();
        verify(userMapper).convertUserListToUserDTOList(userList);
    }

    @Test
    public void testGetUserByMail_whenUserMailExist_itShouldReturnUserDTO() {
        String mail = "test@test.com";
        User user = generateUser(mail);
        UserDTO userDTO = generateUserDTO(mail);

        when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
        when(userMapper.convertUserToUserDTO(user)).thenReturn(userDTO);

        UserDTO userByMail = userService.getUserByMail(mail);

        assertEquals(userDTO, userByMail);
        verify(userRepository).findByMail(mail);
        verify(userMapper).convertUserToUserDTO(user);
    }

    @Test
    public void testGetUserByMail_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException() {
        String mail = "test@test.com";

        when(userRepository.findByMail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserByMail(mail));

        verify(userRepository).findByMail(mail);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void testCreateUser_itShouldReturnCreatedUserDTO() {
        String mail = "test@test.com";
        CreateUserRequest request = new CreateUserRequest(mail, "firstName", "lastName", "");
        User user = new User(mail, "firstName", "lastName", "", false);
        User savedUser = new User(mail, "firstName", "lastName", "", false);
        savedUser.setId(1L);
        UserDTO userDTO = new UserDTO(1L, mail, "firstName", "lastName", "", false);

        when(userMapper.createUserRequestToUser(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn((savedUser));
        when(userMapper.convertUserToUserDTO(savedUser)).thenReturn(userDTO);

        UserDTO result = userService.createUser(request);

        assertEquals(userDTO, result);

        verify(userRepository).save(user);
        verify(userMapper).convertUserToUserDTO(savedUser);
    }

    @Test
    public void testUpdateUser_whenUserMailExistAndUserIsActive_itShouldReturnUpdatedUserDTO() {
        String mail = "test@test.com";
        UpdateUserRequest request = new UpdateUserRequest("firstName2", "lastName2", "middleName");
        User user = new User(mail, "firstName", "lastName", "", true);
        user.setId(1L);
        User updateUser = new User(mail, "firstName2", "lastName2", "middleName", true);
        updateUser.setId(1L);
        User savedUser = new User(mail, "firstName2", "lastName2", "middleName", true);
        savedUser.setId(1L);
        UserDTO userDTO = new UserDTO(1L, mail, "firstName2", "lastName2", "middleName", true);

        when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
        when(userMapper.updateUserRequestToUser(request, user)).thenReturn(updateUser);
        when(userRepository.save(updateUser)).thenReturn((savedUser));
        when(userMapper.convertUserToUserDTO(savedUser)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(mail, request);

        assertEquals(userDTO, result);

        verify(userRepository).save(updateUser);
        verify(userMapper).updateUserRequestToUser(request, user);
        verify(userMapper).convertUserToUserDTO(savedUser);
    }

    @Test
    public void testUpdateUser_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException() {
        String mail = "test@test.com";
        UpdateUserRequest request = new UpdateUserRequest("firstName2", "lastName2", "middleName");

        when(userRepository.findByMail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(mail, request));

        verify(userRepository).findByMail(mail);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void testUpdateUser_whenUserMailExistButUserIsNotActive_itShouldThrowUserNotActiveException() {
        String mail = "test@test.com";
        UpdateUserRequest request = new UpdateUserRequest("firstName2", "lastName2", "middleName");
        User user = new User(mail, "firstName", "lastName", "", false);

        when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));

        assertThrows(UserIsNotActiveException.class, () -> userService.updateUser(mail, request));

        verify(userRepository).findByMail(mail);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void testDeactivateUser_whenUserIdExist_itShouldUpdateUserByActivateFalse() {
        String mail = "test@test.com";
        User user = new User(mail, "firstName", "lastName", "", true);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deactivateUser(userId);

        verify(userRepository).findById(userId);
        assertFalse(user.getIsActive());  // false çekti mi kontrolu yapıldı
        verify(userRepository).save(user);

    }

    @Test
    public void testDeactivateUser_whenUserIdDoesNotExist_itShouldThrowUserNotFoundException() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deactivateUser(userId));

        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void testActivateUser_whenUserIdExist_itShouldUpdateUserByActivateTrue() {
        String mail = "test@test.com";
        User user = new User(mail, "firstName", "lastName", "", false);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.activeUser(userId);

        verify(userRepository).findById(userId);
        assertTrue(user.getIsActive());  // true çekti mi kontrolu yapıldı
        verify(userRepository).save(user);

    }

    @Test
    public void testActivateUser_whenUserIdDoesNotExist_itShouldThrowUserNotFoundException() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.activeUser(userId));

        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void testDeleteUser_whenUserIdExist_itShouldDeleteUser() {
        String mail = "test@test.com";
        User user = new User(mail, "firstName", "lastName", "", false);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);

    }

    @Test
    public void testDeleteUser_whenUserIdDoesNotExist_itShouldThrowUserNotFoundException() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));

        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);

    }


}