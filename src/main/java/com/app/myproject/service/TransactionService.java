package com.app.myproject.service;


import com.app.myproject.entity.*;
import com.app.myproject.exceptionhandling.exceptions.*;
import com.app.myproject.model.enums.TransactionStatus;
import com.app.myproject.model.enums.TransactionType;
import com.app.myproject.exceptions.*;
import com.app.myproject.repo.BalanceRepository;
import com.app.myproject.repo.CommandRepository;
import com.app.myproject.repo.UserCommandTransactionRepository;
import com.app.myproject.repo.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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


        Balance balanceReceiver = balanceRepository.getUserBalanceByUserName(receiver.replace(".",""))
                .orElseGet(()->balanceRepository.getUserBalanceByUserName(receiver.toLowerCase().replace(".","")).orElseThrow(UserNotFoundException::new));

        GenericTransaction genericTransaction = new GenericTransaction();
        genericTransaction.setTransactionType(TransactionType.USER_TO_USER_TRANSACTION);

        UserTransaction userTransaction = UserTransaction.builder()
                .amount(amount).sender(balanceSender).receiver(balanceReceiver)
                .creationTime(LocalDateTime.now()).transaction(genericTransaction).build();
        try {
            userTransaction.setStatus(TransactionStatus.SUCCESSFUL);
            balanceService.transferMoney(sender,balanceReceiver.getUser().getUsername(),balanceSender,balanceReceiver,amount);
        }catch (NotFriendsException | InsufficientBalanceException e) {
            userTransaction.setStatus(TransactionStatus.DECLINED);

        }
        userTransactionRepository.save(userTransaction);
    }

    @Transactional
    public void buyCommand(String username,String id) {
        Balance balance = balanceRepository.getUserBalanceByUserName(username)
                .orElseThrow(UserNotFoundException::new);

        Command command1 = commandRepository.findById(UUID.fromString(id))
                .orElseThrow(CommandNotFoundException::new);

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
            balanceService.buyCommand(balance.getUser().getUsername(),id,balance);

        }catch(InsufficientBalanceException | CommandAlreadyBoughtException e) {
            transaction.setStatus(TransactionStatus.DECLINED);
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED);
        }
        userCommandTransactionRepository.save(transaction);
    }


}
