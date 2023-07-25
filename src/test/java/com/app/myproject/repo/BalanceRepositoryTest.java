package com.app.myproject.repo;

import com.app.myproject.entity.Balance;
import com.app.myproject.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findBalanceByUserName_True() {
        Balance balance = Balance.builder().lastUpdated(LocalDateTime.now()).money(BigDecimal.valueOf(555)).build();

        User user = User.builder().username("vitali").phone("55611").email("vc@gma").balance(balance).build();

        userRepository.save(user);

        Optional<Balance> balanceOptional = balanceRepository.getUserBalanceByUserName("vitali");

        assertTrue(balanceOptional.isPresent());
        assertEquals(balanceOptional.get(),balance);

    }
    @Test
    public void findBalanceByUserName_False1() {
        Optional<Balance> balanceOptional = balanceRepository.getUserBalanceByUserName("veto");
        assertFalse(balanceOptional.isPresent());

    }
    @Test
    public void findByUserName_False2() {
        Balance balance = Balance.builder().lastUpdated(LocalDateTime.now()).money(BigDecimal.valueOf(555)).build();
        balanceRepository.save(balance);
        User user = User.builder().username("vitali").phone("55611").email("vc@gma").balance(null).build();
        userRepository.save(user);

        Optional<Balance> balanceOptional = balanceRepository.getUserBalanceByUserName("vitali");
        assertFalse(balanceOptional.isPresent());


    }



}