package com.app.myproject.service;


import com.app.myproject.entity.*;
import com.app.myproject.enums.TransactionStatus;
import com.app.myproject.enums.TransactionType;
import com.app.myproject.exceptions.*;
import com.app.myproject.repo.BalanceRepository;
import com.app.myproject.repo.CommandRepository;
import com.app.myproject.repo.UserCommandTransactionRepository;
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
public class TransactionService {

    private final BalanceService balanceService;
    private final BalanceRepository balanceRepository;
    private final UserTransactionRepository userTransactionRepository;
    private final CommandRepository commandRepository;
    private final UserCommandTransactionRepository userCommandTransactionRepository;



    @Transactional(propagation = Propagation.REQUIRED)
    public void transferMoney(String sender,String receiver,@PositiveOrZero BigDecimal amount) {
        Optional<Balance> senderOptional = balanceRepository.getUserBalanceByUserName(sender);
        Optional<Balance> receiverOptional = balanceRepository.getUserBalanceByUserName(receiver);
        Balance balanceSender = senderOptional.orElseThrow(UserNotFoundException::new);
        Balance balanceReceiver = receiverOptional.orElseThrow(UserNotFoundException::new);
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setAmount(amount);
        userTransaction.setSender(balanceSender);
        userTransaction.setReceiver(balanceReceiver);
        userTransaction.setCreationTime(LocalDateTime.now());
        GenericTransaction genericTransaction = new GenericTransaction();
        genericTransaction.setTransactionType(TransactionType.USER_TO_USER_TRANSACTION);
        userTransaction.setTransaction(genericTransaction);
        try {
            userTransaction.setStatus(TransactionStatus.SUCCESSFUL);
            balanceService.transferMoney(sender,receiver,balanceSender,balanceReceiver,amount);
        }catch (NotFriendsException | InsufficientBalanceException e) {
            userTransaction.setStatus(TransactionStatus.DECLINED);
        }
        userTransactionRepository.save(userTransaction);
        System.out.println(TransactionStatus.DECLINED.ordinal());
    }

    @Transactional
    public void buyCommand(String username,String command) {
        Optional<Balance> balanceOptional = balanceRepository.getUserBalanceByUserName(username);
        Optional<Command> commandOptional = commandRepository.findByName(command);
        Balance balance = balanceOptional.orElseThrow(UserNotFoundException::new);
        Command command1 = commandOptional.orElseThrow(CommandNotFoundException::new);
        UserCommandTransaction transaction = new UserCommandTransaction();
        transaction.setCommand(command1);
        transaction.setBuyer(balance);
        transaction.setCreationTime(LocalDateTime.now());
        GenericTransaction genericTransaction = new GenericTransaction();
        genericTransaction.setTransactionType(TransactionType.USER_COMMAND_TRANSACTION);
        transaction.setTransaction(genericTransaction);
        transaction.setAmount(command1.getPrice());
        try {
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            balanceService.buyCommand(username,command);

        }catch(InsufficientBalanceException | CommandAlreadyBoughtException e) {
            transaction.setStatus(TransactionStatus.DECLINED);
        }
        userCommandTransactionRepository.save(transaction);
    }


}
