package com.app.myproject.mapper;

import com.app.myproject.dto.UserDTO;
import com.app.myproject.entity.User;
import com.app.myproject.param.UserParam;

public class UserMapper {


    public static UserDTO userToUserDTO(User user) {
        return UserDTO.builder().email(user.getEmail()).phone(user.getPhone()).username(user.getUsername()).balance(user.getBalance().getMoney()).build();
    }


    public static User paramTouser(UserParam userParam) {
        return User.builder().email(userParam.getEmail()).username(userParam.getName())
                .phone(userParam.getPhone()).build();
    }

}
