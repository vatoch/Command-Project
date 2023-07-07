package com.app.myproject.service;

import com.app.myproject.entity.Balance;
import com.app.myproject.entity.User;
import com.app.myproject.entity.UserFriend;
import com.app.myproject.enums.FriendshipStatus;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.UserFriendRepository;
import com.app.myproject.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repo;
    private final UserFriendRepository userFriendRepository;


    @Transactional
    public void registerUser(User user) {
        Balance balance = new Balance();
        balance.setMoney(BigDecimal.valueOf(0));
        balance.setUser(user);
        balance.setLastUpdated(LocalDateTime.now());
        user.setBalance(balance);
        repo.save(user);

    }

    @Transactional
    public void sendFriendRequest(String sender,String receiver){
        Optional<UserFriend> userF1 = userFriendRepository.finDbyUserNames(sender,receiver);
        Optional<UserFriend> userF2 = userFriendRepository.finDbyUserNames(receiver,sender);
        if(userF1.isPresent() || userF2.isPresent()) {
            UserFriend userFriend1 = userF1.orElseGet(userF2::get);
            if(userFriend1.getStatus()==FriendshipStatus.FRIENDS) {
                throw new UnsupportedOperationException("User already in friend list");
            } else if(userFriend1.getStatus()==FriendshipStatus.PENDING) {
                throw new UnsupportedOperationException("Pending request");
            } else {
                userFriend1.setStatus(FriendshipStatus.PENDING);
                return;

            }
        }
        Optional<User> user1 = repo.findByUsername(sender);
        Optional<User> user2 = repo.findByUsername(receiver);
        if(user1.isEmpty()||user2.isEmpty()) {
            throw new UserNotFoundException();

        }

        UserFriend userfriend2 = UserFriend.builder().status(FriendshipStatus.PENDING).date(LocalDateTime.now()).receiver(user2.get())
                .sender(user1.get()).build();
        userFriendRepository.save(userfriend2);


    }

    @Transactional
    public void acceptFriendRequest(String sender,String receiver) {
        Optional<UserFriend> userFriend = userFriendRepository.finDbyUserNames(sender,receiver);
        UserFriend userFriend1 = userFriend.orElseThrow(UserNotFoundException::new);


        if(userFriend1.getStatus()!=FriendshipStatus.PENDING) {
            throw new UnsupportedOperationException("Can't accept friend request");
        }


        userFriend1.setStatus(FriendshipStatus.FRIENDS);
        userFriend1.setDate(LocalDateTime.now());

    }

    @Transactional
    public void rejectFriendRequest(String sender,String receiver) {
        Optional<UserFriend> userFriend = userFriendRepository.finDbyUserNames(receiver,sender);
        UserFriend userFriend1 = userFriend.orElseThrow(UserNotFoundException::new);
        if(userFriend1.getStatus()!=FriendshipStatus.PENDING) {
            throw new UnsupportedOperationException("Can't reject friend request");
        }
        userFriend1.setStatus(FriendshipStatus.DELETED);
        userFriend1.setDate(LocalDateTime.now());
    }

    @Transactional
    public void deleteFriend(String sender,String receiver) {
        Optional<UserFriend> userFriend1 = userFriendRepository.finDbyUserNames(sender,receiver);
        Optional<UserFriend> userFriend2 = userFriendRepository.finDbyUserNames(receiver,sender);
        if(userFriend1.isEmpty()&&userFriend2.isEmpty()) {
            throw new UnsupportedOperationException("Can't delete friend doesn't exist");
        }
        if(userFriend1.isPresent()) {
            if(userFriend1.get().getStatus()==FriendshipStatus.DELETED||userFriend1.get().getStatus()==FriendshipStatus.PENDING) {
                throw new UnsupportedOperationException("Already deleted or pending request");
            }
            userFriend1.get().setStatus(FriendshipStatus.DELETED);
        } else  {
            if(userFriend2.get().getStatus()==FriendshipStatus.DELETED||userFriend2.get().getStatus()==FriendshipStatus.PENDING) {
                throw new UnsupportedOperationException("Already deleted or pending request");
            }
            userFriend2.get().setStatus(FriendshipStatus.DELETED);

        }


    }
    @Transactional
    public List<User> getFriends(String username) {
        Optional<List<UserFriend>> userFriends = userFriendRepository.getFriends(username);
        List<UserFriend> userFriends1 = userFriends.orElseGet(List::of);
        List<User> users = new ArrayList<>();
        userFriends1.forEach(x-> {
            if(x.getSender().getUsername().equals(username)) {
                users.add(x.getReceiver());
            } else {
                users.add(x.getSender());
            }
        });
        return users;

    }




}







