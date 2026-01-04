package com.omer.secondhand.user.dto;

import com.omer.secondhand.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {
    public UserDTOkt convert(User from){
        return new UserDTOkt(from.getMail(), from.getFirstName(), from.getLastName(), from.getMiddleName());
    }
}
