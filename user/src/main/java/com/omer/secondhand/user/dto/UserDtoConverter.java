package com.omer.secondhand.user.dto;

import com.omer.secondhand.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {
    public UserDTO convert(User from){
        return new UserDTO(from.getMail(), from.getFirstName(), from.getLastName(), from.getMiddleName());
    }
}
