package com.omer.secondhand.user.mapper;

import com.omer.secondhand.user.dto.CreateUserRequest;
import com.omer.secondhand.user.dto.UpdateUserRequest;
import com.omer.secondhand.user.dto.UserDTO;
import com.omer.secondhand.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "deletedDate", ignore = true)
    User createUserRequestToUser(CreateUserRequest createUserRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "deletedDate", ignore = true)
    @Mapping(target = "mail", ignore = true)
    User updateUserRequestToUser(UpdateUserRequest updateUserRequest,  @MappingTarget User user);

    UserDTO convertUserToUserDTO(User user);
}
