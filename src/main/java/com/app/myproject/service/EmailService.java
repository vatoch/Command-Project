package com.app.myproject.service;

import com.app.myproject.entity.User;
import com.app.myproject.exceptionhandling.exceptions.CommandNotOwnedException;
import com.app.myproject.exceptionhandling.exceptions.NotFriendsException;
import com.app.myproject.exceptionhandling.exceptions.UserNotFoundException;
import com.app.myproject.repo.UserCommandRepository;
import com.app.myproject.repo.UserFriendRepository;
import com.app.myproject.repo.UserRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EmailService  {

    private final UserFriendRepository userFriendRepository;
    private final UserRepository userRepository;
    private final UserCommandRepository userCommandRepository;
    private final JavaMailSender javaMailSender;


    @Transactional
    public void sendEmail(String username,String receiverUsername,String text)  {

            if (!userRepository.existsByUsername(username)) {
                throw new UserNotFoundException();
            }

            User receiver = userRepository.findByUsername(receiverUsername.replace(".", "")).orElseGet(
                    () -> userRepository.findByUsername(receiverUsername.toLowerCase().replace(".", "")).orElseThrow(UserNotFoundException::new)
            );
            String receiverUsernameReal = receiver.getUsername();

            if (!userCommandRepository.existsByUsernameAndCommandName(username, "Send email")) {
                throw new CommandNotOwnedException();
            }

            if (!userFriendRepository.existsFriends(username, receiverUsernameReal)) {
                throw new NotFriendsException();
            }


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom("vatochitaia6@gmail.com");
            mimeMessageHelper.setTo(receiver.getEmail());
            mimeMessageHelper.setSubject("null");
            mimeMessageHelper.setText(text);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }



}
