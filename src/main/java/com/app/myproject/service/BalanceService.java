package com.app.myproject.service;

import com.app.myproject.entity.*;
import com.app.myproject.enums.FriendshipStatus;
import com.app.myproject.enums.TransactionStatus;
import com.app.myproject.enums.TransactionType;
import com.app.myproject.exceptions.*;
import com.app.myproject.repo.*;
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
    private final UserFriendRepository userFriendRepository;
    private final CommandRepository commandRepository;
    private final UserCommandRepository userCommandRepository;
    private final UserRepository userRepository;
    private final UserFillBalanceRepository userFillBalanceRepository;




    @Transactional
    public void fillBalance(String username, String stringAmount) {
        BigDecimal amount = new BigDecimal(stringAmount);
        Optional<Balance> balanceOptional = balanceRepository.getUserBalanceByUserName(username);
        Balance balance = balanceOptional.orElseThrow(UserNotFoundException::new);
        balance.setMoney(balance.getMoney().add(amount));
        balance.setLastUpdated(LocalDateTime.now());
        GenericTransaction genericTransaction = new GenericTransaction();
        genericTransaction.setTransactionType(TransactionType.FILL_BALANCE_TRANSACTION);

        UserFillBalanceTransaction transaction = UserFillBalanceTransaction.builder().status(TransactionStatus.SUCCESSFUL).amount(amount)
                .transaction(genericTransaction).time(LocalDateTime.now()).balance(balance).build();
        userFillBalanceRepository.save(transaction);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void transferMoney(String sender,String receiver,Balance balanceSender,Balance balanceReceiver,@PositiveOrZero BigDecimal amount) {

        Optional<UserFriend> userFriendOptional1 = userFriendRepository.finDbyUserNames(sender,receiver);
        Optional<UserFriend> userFriendOptional2 = userFriendRepository.finDbyUserNames(receiver,sender);
        if(userFriendOptional2.isEmpty()&&userFriendOptional1.isEmpty()) {
            throw new NotFriendsException();
        }
        UserFriend userFriend = userFriendOptional1.orElseGet(userFriendOptional2::get);


        if(userFriend.getStatus()!= FriendshipStatus.FRIENDS) {

            throw new NotFriendsException();

        }

        if(balanceSender.getMoney().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();

        }
        balanceSender.setMoney(balanceSender.getMoney().subtract(amount));
        balanceReceiver.setMoney(balanceReceiver.getMoney().add(amount));



    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void buyCommand(String username,String command) {
        Optional<Command> command1 = commandRepository.findByName(command);
        Optional<Balance> balanceOptional = balanceRepository.getUserBalanceByUserName(username);
        Optional<User> userOptional = userRepository.findByUsername(username);

        Command command2 = command1.orElseThrow(CommandNotFoundException::new);
        Balance balance = balanceOptional.orElseThrow(UserNotFoundException::new);

        if(balance.getMoney().compareTo(command2.getPrice())==-1) {
            throw new InsufficientBalanceException();

        }
        Optional<UserCommand> userCommandOptional = userCommandRepository.findByUsernameAndCommandName(username,command);
        if(userCommandOptional.isPresent()) {
            throw new CommandAlreadyBoughtException();

        }
        UserCommand userCommand = new UserCommand();
        userCommand.setCommand(command2);
        userCommand.setUser(userOptional.orElseThrow(UserNotFoundException::new));
        balance.setMoney(balance.getMoney().subtract(command2.getPrice()));




    }






}
