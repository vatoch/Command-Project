package com.app.myproject.service;

import com.app.myproject.entity.Command;
import com.app.myproject.entity.User;
import com.app.myproject.entity.UserFriend;
import com.app.myproject.exceptionhandling.exceptions.*;
import com.app.myproject.model.enums.CommandType;
import com.app.myproject.model.enums.FriendshipStatus;
import com.app.myproject.repo.CommandRepository;
import com.app.myproject.repo.UserFriendRepository;
import com.app.myproject.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommandRepository commandRepository;

    @Mock
    private UserFriendRepository userFriendRepository;

    @InjectMocks
    private UserService userService;


    @Test
    public void registerUser_Successful() {

        User user = User.builder().username("va").phone("3").email("v").build();


        when(userRepository.existsByUsername("va")).thenReturn(false);
        UUID commandId = UUID.fromString("b94b91be-0b05-4673-9126-eb8add54a726");
        Command command = Command.builder().id(commandId).name("Deposit money").type(CommandType.PRICED).build();

        when(commandRepository.findById(commandId)).thenReturn(Optional.of(command));


        userService.registerUser(user);

        assertNotNull(user.getBalance());
        assertEquals(user.getBalance().getMoney(), BigDecimal.valueOf(1000));
        assertNotNull(user.getBalance().getLastUpdated());
        assertEquals(command,user.getCommands().get(0).getCommand());




    }
    @Test
    public void registerUser_UserAlreadyExists() {

        User user = User.builder().username("va").phone("3").email("v").build();

        when(userRepository.existsByUsername("va")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,()->userService.registerUser(user));

        verify(userRepository,never()).save(user);





    }
    @Test
    public void sendFriendRequest_Successful() {
        UUID uuid = UUID.randomUUID();
        User user1 = User.builder().username("va").phone("3").email("v3").build();
        User user2 = User.builder().id(uuid).username("kva").phone("32").email("v11").build();

        when(userRepository.findByUsername("va")).thenReturn(Optional.of(user1));
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user2));
        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).build();
        when(userFriendRepository.findByUsernameAndId("va",user2.getId())).thenReturn(Optional.of(userFriend));
        userService.sendFriendRequest("va",uuid.toString());

        verify(userFriendRepository,times(1)).save(userFriend);



    }
    @Test
    public void sendFriendRequest_Successful2() {
        UUID uuid = UUID.randomUUID();
        User user1 = User.builder().username("va").phone("3").email("v3").build();
        User user2 = User.builder().id(uuid).username("kva").phone("32").email("v11").build();

        when(userRepository.findByUsername("va")).thenReturn(Optional.of(user1));
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user2));
        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.DELETED).build();
        when(userFriendRepository.findByUsernameAndId("va",user2.getId())).thenReturn(Optional.of(userFriend));
        userService.sendFriendRequest("va",uuid.toString());

        verify(userFriendRepository,times(1)).save(userFriend);
    }
    @Test

    public void sendFriendRequest_SenderNotFound() {
        when(userRepository.findByUsername("va")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.sendFriendRequest("va",UUID.randomUUID().toString()));


    }
    @Test
    public void sendFriendRequest_ReceiverNotFound() {
          User user1 = User.builder().username("va").phone("3").email("v3").build();
          UUID uuid = UUID.randomUUID();
          when(userRepository.findByUsername("va")).thenReturn(Optional.of(user1));
          when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.sendFriendRequest("va",uuid.toString()));


    }
    @Test
    public void sendFriendRequest_RequestPending() {
        UUID uuid = UUID.randomUUID();
        User user1 = User.builder().username("va").phone("3").email("v3").build();
        User user2 = User.builder().id(uuid).username("kva").phone("32").email("v11").build();

        when(userRepository.findByUsername("va")).thenReturn(Optional.of(user1));
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user2));
        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.PENDING).build();
        when(userFriendRepository.findByUsernameAndId("va",user2.getId())).thenReturn(Optional.of(userFriend));
        assertThrows(FriendshipStatusPendingException.class,()->userService.sendFriendRequest("va",uuid.toString()));

    }
    @Test

    public void sendFriendRequest_AlreadyFriends() {
        UUID uuid = UUID.randomUUID();
        User user1 = User.builder().username("va").phone("3").email("v3").build();
        User user2 = User.builder().id(uuid).username("kva").phone("32").email("v11").build();

        when(userRepository.findByUsername("va")).thenReturn(Optional.of(user1));
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user2));
        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.FRIENDS).build();
        when(userFriendRepository.findByUsernameAndId("va",user2.getId())).thenReturn(Optional.of(userFriend));
        assertThrows(AlreadyFriendsException.class,()->userService.sendFriendRequest("va",uuid.toString()));
    }


    @Test
    public void acceptFriendRequest_Successful() {
        UUID uuid = UUID.randomUUID();
        User user1 = User.builder().username("va").phone("3").email("v3").build();
        User user2 = User.builder().id(uuid).username("kva").phone("32").email("v11").build();

        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.PENDING).build();
        when(userFriendRepository.findByUsAndId("va",uuid)).thenReturn(Optional.of(userFriend));

        userService.acceptFriendRequest("va",uuid.toString());

    }
    @Test
    public void acceptFriendRequest_NotFound() {
        UUID rand = UUID.randomUUID();

        assertThrows(FriendRequestNotFoundException.class, () -> userService.acceptFriendRequest("kva", rand.toString()));


    }
    @Test
    public void acceptFriendRequest_NotPending1() {
        UUID uuid = UUID.randomUUID();
        User user1 = User.builder().username("va").phone("3").email("v3").build();
        User user2 = User.builder().id(uuid).username("kva").phone("32").email("v11").build();

        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.DELETED).build();

        when(userFriendRepository.findByUsAndId("va",uuid)).thenReturn(Optional.of(userFriend));

        assertThrows(FriendRequestNotFoundException.class,()->userService.acceptFriendRequest("va",uuid.toString()));

    }
    @Test
    public void acceptFriendRequest_NotPending2() {
        UUID uuid = UUID.randomUUID();
        User user1 = User.builder().username("va").phone("3").email("v3").build();
        User user2 = User.builder().id(uuid).username("kva").phone("32").email("v11").build();

        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.FRIENDS).build();

        when(userFriendRepository.findByUsAndId("va",uuid)).thenReturn(Optional.of(userFriend));

        assertThrows(FriendRequestNotFoundException.class,()->userService.acceptFriendRequest("va",uuid.toString()));

    }






}