package com.app.myproject.controller;


import com.app.myproject.entity.User;
import com.app.myproject.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody User user) {
        service.registerUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friendrequest/send/{receiver}")
    public ResponseEntity<Void> sendFriendRequest(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        service.sendFriendRequest(username,receiver);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/friendrequest/accept/{receiver}")
    public ResponseEntity<Void> acceptFriendRequest(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        service.acceptFriendRequest(username,receiver);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/friendrequest/reject/{receiver}")
    public ResponseEntity<Void> rejectFriendRequest(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        service.rejectFriendRequest(username,receiver);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/delete/{receiver}")
    public ResponseEntity<Void> deleteFromFriends(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        service.deleteFriend(username,receiver);
        return ResponseEntity.ok().build();
    }



}
