package com.app.myproject.service;

import com.app.myproject.entity.User;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserRepository userRepository;



    public void sendMessage(String sender,String receiver,String message) {
        Optional<User> user1 = userRepository.findByUsername(sender);
        Optional<User> user2 = userRepository.findByUsername(receiver);
        User senderUser = user1.orElseThrow(UserNotFoundException::new);
        User receiverUser = user2.orElseThrow(UserNotFoundException::new);

    }

}
