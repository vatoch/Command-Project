package com.app.myproject.service;


import com.app.myproject.entity.Balance;
import com.app.myproject.entity.GenericTransaction;
import com.app.myproject.entity.UserTransaction;
import com.app.myproject.enums.TransactionStatus;
import com.app.myproject.enums.TransactionType;
import com.app.myproject.exceptions.InsufficientBalanceException;
import com.app.myproject.exceptions.NotFriendsException;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.BalanceRepository;
import com.app.myproject.repo.UserTransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
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
            balanceService.transferMoney(sender,receiver,amount);
        }catch (NotFriendsException | InsufficientBalanceException e) {
            userTransaction.setStatus(TransactionStatus.DECLINED);
        }
        userTransactionRepository.save(userTransaction);
        System.out.println(TransactionStatus.DECLINED.ordinal());
    }


}
