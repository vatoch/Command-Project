package com.app.myproject.service;

import com.app.myproject.entity.User;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService{

    private final UserRepository userRepository;


    @Transactional
    public void sendMessage(String sender,String receiver,String message) {
        Optional<User> user1 = userRepository.findByUsername(sender);
        Optional<User> user2 = userRepository.findByUsername(receiver.replace(".","").toLowerCase());
        User senderUser = user1.orElseThrow(UserNotFoundException::new);
        User receiverUser = user2.orElseThrow(UserNotFoundException::new);

        System.out.println("Sender:" +senderUser.getUsername() + "\n "+ "Receiver :" + receiverUser.getUsername() + "\n" + message);

    }




}
