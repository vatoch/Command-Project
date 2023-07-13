package com.app.myproject.service;

import com.app.myproject.entity.User;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.UserRepository;
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


        User senderUser = userRepository.findByUsername(sender).orElseThrow(UserNotFoundException::new);
        User receiverUser = userRepository.findByUsername(receiver.replace(".","")).orElseGet(
                ()->userRepository.findByUsername(receiver.toLowerCase().replace(".","")).orElseThrow(UserNotFoundException::new)
        );

        System.out.println("Sender:" +senderUser.getUsername() + "\n "+ "Receiver :" + receiverUser.getUsername() + "\n" + message);

    }




}
