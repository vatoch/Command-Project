package com.app.myproject.service;

import com.app.myproject.entity.User;
import com.app.myproject.exceptionhandling.exceptions.UserNotFoundException;
import com.app.myproject.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    public void sendMessage_BothUsersExist() {
        User user1 = User.builder()
                .username("vatuli")
                .phone("whatsap")
                .email("vc@gmail.com").build();
        User user2 = User.builder()
                .username("satuli")
                .phone("c")
                .email("z").build();

        when(userRepository.findByUsername("vatuli")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("satuli")).thenReturn(Optional.of(user2));

        messageService.sendMessage("vatuli","satuli","whatsap");


    }
    @Test
    public void sendMessage_ReceiverDoesntExist() {
        User user1 = User.builder()
                .username("vatuli")
                .phone("whatsap")
                .email("vc@gmail.com").build();

        when(userRepository.findByUsername("vatuli")).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("zxc")).thenReturn(Optional.empty());
        UserNotFoundException expectedException = new UserNotFoundException();

        Throwable exception = assertThrows(UserNotFoundException.class, () -> {
            messageService.sendMessage("vatuli", "zxc", "Test message");
        });

        assertEquals(expectedException.getClass(), exception.getClass());


    }
    @Test
    public void sendMessage_SenderDoesntExist() {

        when(userRepository.findByUsername("vato1")).thenReturn(Optional.empty());

        UserNotFoundException expectedException = new UserNotFoundException();
        Throwable exception = assertThrows(UserNotFoundException.class, () -> {
            messageService.sendMessage("vato1", "vato2", "Test message");
        });

        assertEquals(expectedException.getClass(), exception.getClass());



    }


}