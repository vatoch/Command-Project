package com.app.myproject.service;

import com.app.myproject.exceptionhandling.exceptions.*;
import com.app.myproject.model.dto.CommandDTO;
import com.app.myproject.model.dto.UserDTO;
import com.app.myproject.entity.*;
import com.app.myproject.model.enums.FriendshipStatus;
import com.app.myproject.exceptions.*;
import com.app.myproject.mapper.CommandMapper;
import com.app.myproject.mapper.UserMapper;
import com.app.myproject.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repo;
    private final UserFriendRepository userFriendRepository;
    private final CommandRepository commandRepository;
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final UserCommandRepository userCommandRepository;


    @Transactional
    public void registerUser(User user) {
        Balance balance = new Balance();
        balance.setMoney(BigDecimal.valueOf(1000));
        balance.setUser(user);
        balance.setLastUpdated(LocalDateTime.now());
        user.setBalance(balance);
        UserCommand userCommand = new UserCommand();
        userCommand.setUser(user);
        userCommand.setCommand(commandRepository.findById(UUID.fromString("b94b91be-0b05-4673-9126-eb8add54a726")).orElseThrow(CommandNotFoundException::new));
        user.setCommands(new ArrayList<>());
        user.getCommands().add(userCommand);
        repo.save(user);

    }

    @Transactional
    public void sendFriendRequest(String sender,String id){
        User user1 = repo.findByUsername(sender).orElseThrow(UserNotFoundException::new);
        User user2 = repo.findById(UUID.fromString(id)).orElseThrow(UserNotFoundException::new);

        UserFriend userFriend = userFriendRepository.findByUsernameAndId(sender,UUID.fromString(id)).orElseGet(UserFriend::new);

        if(userFriend.getStatus()!=null) {
            if(userFriend.getStatus().equals(FriendshipStatus.FRIENDS)) {
                throw new AlreadyFriendsException();
            } else if(userFriend.getStatus().equals(FriendshipStatus.PENDING)) {
                throw new FriendshipStatusPendingException();
            } else {
                userFriend.setStatus(FriendshipStatus.PENDING);
            }
        } else {
            userFriend.setReceiver(user2);
            userFriend.setSender(user1);
            userFriend.setStatus(FriendshipStatus.PENDING);
        }


       userFriendRepository.save(userFriend);


    }

    @Transactional
    public void acceptFriendRequest(String sender,String id) {
        UserFriend userFriend1 = userFriendRepository.findByUsAndId(sender,UUID.fromString(id)).orElseThrow(UnsupportedOperationException::new);
        if(!userFriend1.getStatus().equals(FriendshipStatus.PENDING)) {
            throw new UnsupportedOperationException();
        }

        userFriend1.setStatus(FriendshipStatus.FRIENDS);
        userFriend1.setDate(LocalDateTime.now());

    }

    @Transactional
    public void rejectFriendRequest(String sender,String id) {

        UserFriend userFriend1 = userFriendRepository.findByUsAndId(sender,UUID.fromString(id)).orElseThrow(UnsupportedOperationException::new);
        if(userFriend1.getStatus()!=FriendshipStatus.PENDING) {
            throw new UnsupportedOperationException("Can't reject friend request");
        }
        userFriend1.setStatus(FriendshipStatus.DELETED);
        userFriend1.setDate(LocalDateTime.now());
    }

    @Transactional
    public void deleteFriend(String sender,String id) {
        UserFriend userFriend1 = userFriendRepository.findByUsernameAndId(sender,UUID.fromString(id)).orElseThrow(NotFriendsException::new);

        if(!userFriend1.getStatus().equals(FriendshipStatus.FRIENDS)) {
            throw new CantDeleteFriendException();
        }

        userFriend1.setStatus(FriendshipStatus.DELETED);


    }
    @Transactional
    public Page<User> getFriends(String username,Pageable pageable) {
        if(!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException();
        }
        return userFriendRepository.getFriends2(username,pageable);

    }


    public Page<CommandDTO> getMyCommands(String username,Pageable pageable) {
        return commandRepository.getCommandsByUsername(username,pageable).map(CommandMapper::commandToDto);
    }


    public UserDTO viewProfile(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return UserMapper.userToUserDTO(user);
    }

    @Transactional
    public void fillBalance(String username,String money) {
        BigDecimal bigDecimal = new BigDecimal(money);
        if(bigDecimal.compareTo(BigDecimal.ZERO)<0) {
            throw new IllegalArgumentException();
        }
        Balance balance = balanceRepository.getUserBalanceByUserName(username).orElseThrow(UserNotFoundException::new);
        balance.setMoney(balance.getMoney().add(bigDecimal));
    }

    public void deleteAccount(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);

    }




}







