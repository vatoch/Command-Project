package com.app.myproject.controller;

import com.app.myproject.entity.Command;
import com.app.myproject.entity.User;
import com.app.myproject.repo.CommandRepository;
import com.app.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CommandRepository repository;
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok().build();

    }
    @PostMapping("/friendrequest/send/{receiver}")
    public ResponseEntity<Void> sendFriendRequest(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        userService.sendFriendRequest(username,receiver);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/friendrequest/accept/{receiver}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable String receiver,@RequestHeader(name = "username") String username) {
        userService.acceptFriendRequest(receiver,username);
        return ResponseEntity.ok().build();

    }
    @GetMapping("/friends")
    public ResponseEntity<List<User>> getFriends(@RequestHeader(name = "username") String username) {
        return ResponseEntity.ok(userService.getFriends(username));
    }

    @PostMapping("/friendrequest/reject/{receiver}")
    public ResponseEntity<Void> rejectFriendRequest(@RequestHeader(name = "username") String username , @PathVariable String receiver) {
        userService.rejectFriendRequest(username,receiver);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{receiver}")
    public ResponseEntity<Void> deleteFriend(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        userService.deleteFriend(username,receiver);
        return ResponseEntity.ok().build();

    }

}
