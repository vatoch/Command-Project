package com.app.myproject.facade;

import com.app.myproject.dto.UserDTO;
import com.app.myproject.entity.User;
import com.app.myproject.mapper.UserMapper;
import com.app.myproject.param.UserParam;
import com.app.myproject.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@RequiredArgsConstructor
@Setter
public class UserFacade {

    private final UserService userService;

    public void registerUser(UserParam userParam) {
        userService.registerUser(UserMapper.paramTouser(userParam));
    }

    public UserDTO viewProfile(String username) {
        return userService.viewProfile(username);
    }

    public void fillBalance(String username,String money) {
        userService.fillBalance(username,money);
    }

    public void friendRequestResponse(String username,Integer response,String id) {
        if(response.equals(0)) {
            userService.rejectFriendRequest(username,id);
        } else {
            userService.acceptFriendRequest(username,id);
        }
    }



}
