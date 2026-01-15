package com.omer.secondhand.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long id;
    private String mail;
    private String firstName;
    private String lastName;
    private String middleName;
    private Boolean isActive;
}
