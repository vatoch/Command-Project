package com.app.myproject.service;


import com.app.myproject.entity.*;
import com.app.myproject.enums.TransactionStatus;
import com.app.myproject.enums.TransactionType;
import com.app.myproject.exceptions.*;
import com.app.myproject.repo.BalanceRepository;
import com.app.myproject.repo.CommandRepository;
import com.app.myproject.repo.UserCommandTransactionRepository;
import com.app.myproject.repo.UserTransactionRepository;
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



    @Transactional
    public void transferMoney(String sender,String receiver, String stringAmount) {
        BigDecimal amount = new BigDecimal(stringAmount.substring(0,stringAmount.length()-1));
        Balance balanceSender = balanceRepository.getUserBalanceByUserName(sender).orElseThrow(UserNotFoundException::new);
        Optional<Balance> balanceReceiverOptional1 = balanceRepository.getUserBalanceByUserName(receiver.replace(".",""));
        Optional<Balance> balanceReceiverOptional2 = balanceRepository.getUserBalanceByUserName(receiver.toLowerCase().replace(".",""));
        if(balanceReceiverOptional2.isEmpty()&&balanceReceiverOptional1.isEmpty()) {
            throw new UserNotFoundException();
        }
        Balance balanceReceiver = balanceReceiverOptional1.orElseGet(balanceReceiverOptional2::get);

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
            balanceService.transferMoney(sender,balanceReceiver.getUser().getUsername(),balanceSender,balanceReceiver,amount);
        }catch (NotFriendsException | InsufficientBalanceException e) {
            userTransaction.setStatus(TransactionStatus.DECLINED);
        }
        userTransactionRepository.save(userTransaction);
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
            balanceService.buyCommand(balance.getUser().getUsername(),command,balance);

        }catch(InsufficientBalanceException | CommandAlreadyBoughtException e) {
            transaction.setStatus(TransactionStatus.DECLINED);
        }
        userCommandTransactionRepository.save(transaction);
    }


}
