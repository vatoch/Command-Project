package com.app.myproject.controller;


import com.app.myproject.entity.User;
import com.app.myproject.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/sendrequest/{receiver}")
    public ResponseEntity<Void> sendFriendRequest(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        service.sendFriendRequest(username,receiver);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/acceptrequest/{receiver}")
    public ResponseEntity<Void> acceptFriendRequest(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        service.acceptFriendRequest(username,receiver);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/delete/{receiver}")
    public ResponseEntity<Void> deleteFromFriends(@RequestHeader(name = "username") String username,@PathVariable String receiver) {
        service.deleteFriend(username,receiver);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendRequest(@RequestBody byte[] data) throws IOException , InterruptedException {

        String audio_url = service.getAudioUrl("6fc8664645534ab4974b053cf94e9808",data);

        String id = service.getId("6fc8664645534ab4974b053cf94e9808",audio_url);
        System.out.println(id);


       return ResponseEntity.ok(service.convertAudioToText("6fc8664645534ab4974b053cf94e9808",id));
    }

    @PostMapping("/my")
    public ResponseEntity<String> my(@RequestBody byte[] data) throws IOException, InterruptedException {
        return ResponseEntity.ok(service.getAudioUrl("6fc8664645534ab4974b053cf94e9808",data));
    }

    @GetMapping("/tes/{vatsap}")
    public ResponseEntity<Void> tes(@PathVariable(name = "vatsap") String name) {
        return ResponseEntity.ok().build();
    }





}
