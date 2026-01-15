package com.omer.secondhand.user.service;

import com.omer.secondhand.user.TestSupport;
import com.omer.secondhand.user.dto.UserDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends TestSupport {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    private TestSupport testSupport;

    @InjectMocks
    private UserService userService;


    @Test
    public void testGetAllUsers_itShouldReturnUserDTOList(){
        List<User> userList = generateUsers();
        List<UserDTO> userDTOList = generateUserDTOList(userList);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.convertUserListToUserDTOList(userList)).thenReturn(userDTOList);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(userDTOList,result);
        verify(userRepository).findAll();
        verify(userMapper).convertUserListToUserDTOList(userList);
    }

    @Test
    public void testGetUserByMail_whenUserMailExist_itShouldReturnUserDTO(){
        String mail = "test@test.com";
       User user = generateUser(mail);
       UserDTO userDTO = generateUserDTO(mail);

       when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
       when(userMapper.convertUserToUserDTO(user)).thenReturn(userDTO);

        UserDTO userByMail = userService.getUserByMail(mail);

        assertEquals(userDTO,userByMail);
        verify(userRepository).findByMail(mail);
        verify(userMapper).convertUserToUserDTO(user);
    }

    @Test
    public void testGetUserByMail_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException(){
        String mail = "test@test.com";

        when(userRepository.findByMail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserByMail(mail));

        verify(userRepository).findByMail(mail);
        verifyNoInteractions(userMapper);
    }
}