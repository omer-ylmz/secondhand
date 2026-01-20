package com.omer.secondhand.user;

import com.omer.secondhand.user.dto.UserDTO;
import com.omer.secondhand.user.model.User;


import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSupport {
    public static Long userId = 100L;

    public List<User> generateUsers() {
        return IntStream.range(0, 5).mapToObj(i ->
                new User(
                        i + "@yilmaz.net",
                        "firstName" + i,
                        "lastName" + i,
                        "",
                        new Random(2).nextBoolean())
        ).collect(Collectors.toList());
    }

    public List<UserDTO> generateUserDTOList(List<User> userList) {
        return userList.stream().map(this::generateUserDTOList).collect(Collectors.toList());
    }

    public User generateUser(String mail) {
        return new User(
                mail,
                "firstName" + userId,
                "lastName" + userId,
                "",
                true);
    }

    public UserDTO generateUserDTOList(User user) {
        return new UserDTO(
                user.getId(),
                user.getMail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getIsActive()
        );
    }

    public UserDTO generateUserDTO(String mail) {
        return new UserDTO(
                userId,
                mail,
                "firstName" + userId,
                "lastName" + userId,
                "",
                true);
    }
}
