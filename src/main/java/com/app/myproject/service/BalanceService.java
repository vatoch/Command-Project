package com.app.myproject.service;

import com.app.myproject.entity.Balance;
import com.app.myproject.entity.User;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.BalanceRepository;
import com.app.myproject.repo.UserRepository;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;

    @Transactional
    public void fillBalance(String username,@PositiveOrZero BigDecimal amount) {
        Optional<Balance> balanceOptional = balanceRepository.getUserBalanceByUserName(username);
        Balance balance = balanceOptional.orElseThrow(UserNotFoundException::new);
        balance.setMoney(balance.getMoney().add(amount));
        balance.setLastUpdated(LocalDateTime.now());
    }





}
