package com.app.myproject.service;

import com.app.myproject.entity.User;
import com.app.myproject.entity.UserFriend;
import com.app.myproject.exceptions.NotFriendsException;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.UserFriendRepository;
import com.app.myproject.repo.UserRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final UserFriendRepository userFriendRepository;
    private final UserRepository userRepository;



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendEmail(String username,String receiverUsername,String text,String subject) throws MessagingException {
        Optional<User> user1 = userRepository.findByUsername(username);
        Optional<User> user2 = userRepository.findByUsername(receiverUsername);
        User sender = user1.orElseThrow(UserNotFoundException::new);
        User receiver = user2.orElseThrow(UserNotFoundException::new);
        Optional<UserFriend> userFriend1 = userFriendRepository.finDbyUserNames(username,receiverUsername);
        Optional<UserFriend> userFriend2 = userFriendRepository.finDbyUserNames(receiverUsername,username);
        if(userFriend1.isEmpty()&&userFriend2.isEmpty()) {
            throw new NotFriendsException();

        }
        try {
            Thread.sleep(10000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 587);
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("vatochitaia6@gmail.com", "vwtfsspgxzfhwdfl");
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("vatochitaia6@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(receiver.getEmail()));
        message.setSubject(subject);



        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(text + "\n" + "\n Sent by " + username, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }



}
