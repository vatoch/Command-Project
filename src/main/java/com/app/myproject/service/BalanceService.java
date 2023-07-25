package com.app.myproject.service;

import com.app.myproject.exceptionhandling.exceptions.*;
import com.app.myproject.model.dto.BalanceDTO;
import com.app.myproject.entity.*;
import com.app.myproject.model.enums.FriendshipStatus;
import com.app.myproject.model.enums.TransactionStatus;
import com.app.myproject.model.enums.TransactionType;
import com.app.myproject.mapper.BalanceMapper;
import com.app.myproject.repo.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final UserFriendRepository userFriendRepository;
    private final CommandRepository commandRepository;
    private final UserCommandRepository userCommandRepository;
    private final UserRepository userRepository;
    private final UserFillBalanceRepository userFillBalanceRepository;
    private final Logger logger = LoggerFactory.getLogger("myLog");




    @Transactional
    public void fillBalance(String username, String stringAmount) {
        Balance balance = balanceRepository.getUserBalanceByUserName(username).orElseThrow(UserNotFoundException::new);
        if(userCommandRepository.existsByUsernameAndCommandName(username,"Deposit money")) {
            throw new CommandNotOwnedException();
        }
        BigDecimal amount = new BigDecimal(stringAmount);


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



        if(!userCommandRepository.existsByUsernameAndCommandName(sender,"Transfer money")) {
            throw new CommandNotOwnedException();
        }

        UserFriend userFriend = userFriendRepository.finDbyUserNames(sender,receiver).orElseThrow(NotFriendsException::new);

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
    public void buyCommand(String username,String id,Balance balance) {

        User user= userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        Command command2 = commandRepository.findById(UUID.fromString(id)).orElseThrow(CommandNotFoundException::new);

        if(balance.getMoney().compareTo(command2.getPrice()) < 0) {
            throw new InsufficientBalanceException();
        }
        Optional<UserCommand> userCommandOptional = userCommandRepository.findByUserNameAndId(username,UUID.fromString(id));
        if(userCommandOptional.isPresent()) {
            throw new CommandAlreadyBoughtException();

        }
        UserCommand userCommand = new UserCommand();
        userCommand.setCommand(command2);
        userCommand.setUser(user);
        balance.setMoney(balance.getMoney().subtract(command2.getPrice()));
        userCommandRepository.save(userCommand);




    }


    public BalanceDTO getBalance(String username) {
        Balance balance = balanceRepository.getUserBalanceByUserName(username).orElseThrow(UserNotFoundException::new);
        return BalanceMapper.balanceToDTO(balance);
    }






}
