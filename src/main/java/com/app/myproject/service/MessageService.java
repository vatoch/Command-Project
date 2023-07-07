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
        Optional<User> user1 = userRepository.findByUsername(sender);
        Optional<User> user2 = userRepository.findByUsername(receiver.replace(".",""));
        Optional<User> user3 = userRepository.findByUsername(receiver.toLowerCase().replace(".",""));
        if(user2.isEmpty()&&user3.isEmpty()) {
            throw new UserNotFoundException();
        }
        User senderUser = user1.orElseThrow(UserNotFoundException::new);
        User receiverUser = user2.orElseGet(user3::get);

        System.out.println("Sender:" +senderUser.getUsername() + "\n "+ "Receiver :" + receiverUser.getUsername() + "\n" + message);

    }




}
