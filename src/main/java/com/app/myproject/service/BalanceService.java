package com.app.myproject.service;

import com.app.myproject.entity.*;
import com.app.myproject.enums.FriendshipStatus;
import com.app.myproject.enums.TransactionStatus;
import com.app.myproject.enums.TransactionType;
import com.app.myproject.exceptions.InsufficientBalanceException;
import com.app.myproject.exceptions.NotFriendsException;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.BalanceRepository;
import com.app.myproject.repo.UserFriendRepository;
import com.app.myproject.repo.UserRepository;
import com.app.myproject.repo.UserTransactionRepository;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final UserTransactionRepository userTransactionRepository;


    @Transactional
    public void fillBalance(String username,@PositiveOrZero BigDecimal amount) {
        Optional<Balance> balanceOptional = balanceRepository.getUserBalanceByUserName(username);
        Balance balance = balanceOptional.orElseThrow(UserNotFoundException::new);
        balance.setMoney(balance.getMoney().add(amount));
        balance.setLastUpdated(LocalDateTime.now());
        GenericTransaction genericTransaction = new GenericTransaction();
        genericTransaction.setTransactionType(TransactionType.FILL_BALANCE_TRANSACTION);

        UserFillBalanceTransaction transaction = UserFillBalanceTransaction.builder().status(TransactionStatus.SUCCESSFUL).amount(amount)
                .transaction(genericTransaction).time(LocalDateTime.now()).balance(balance).build();

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void transferMoney(String sender,String receiver,@PositiveOrZero BigDecimal amount) {

        Optional<Balance> senderOptional = balanceRepository.getUserBalanceByUserName(sender);
        Optional<Balance> receiverOptional = balanceRepository.getUserBalanceByUserName(receiver);
        Balance balanceSender = senderOptional.orElseThrow(UserNotFoundException::new);
        Balance balanceReceiver = receiverOptional.orElseThrow(UserNotFoundException::new);
        Optional<UserFriend> userFriendOptional1 = userFriendRepository.finDbyUserNames(sender,receiver);
        Optional<UserFriend> userFriendOptional2 = userFriendRepository.finDbyUserNames(receiver,sender);
        if(userFriendOptional2.isEmpty()&&userFriendOptional1.isEmpty()) {
            throw new NotFriendsException();
        }
        UserFriend userFriend = userFriendOptional1.orElseGet(userFriendOptional2::get);



        if(userFriend.getStatus()!= FriendshipStatus.FRIENDS) {

            throw new NotFriendsException();

        }

        if(balanceSender.getMoney().compareTo(amount)==-1) {
            throw new InsufficientBalanceException();

        }
        balanceSender.setMoney(balanceSender.getMoney().subtract(amount));
        balanceReceiver.setMoney(balanceReceiver.getMoney().add(amount));



    }






}
