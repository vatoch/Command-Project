package com.app.myproject.repo;

import com.app.myproject.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_FindByUserName_True() {
        User user  = User.builder().email("asd@").phone("em3").username("gnqq").build();
        userRepository.save(user);
        Optional<User> user1 = userRepository.findByUsername("gnqq");
        assertTrue(user1.isPresent());
        assertEquals(user,user1.get());
    }
    @Test
    public void UserRepository_FindByUserName_False() {
        Optional<User> user = userRepository.findByUsername("zxc");
        assertFalse(user.isPresent());

    }

    @Test
    public void UserRepository_ExistsByUserName_True() {

        User user = User.builder().username("vc").phone("555").email("23@@").build();
        userRepository.save(user);

        boolean b = userRepository.existsByUsername("vc");
        assertTrue(b);
    }
    @Test
    public void UserRepository_ExistsByUserName_False() {
        boolean b = userRepository.existsByUsername("ac");
        assertFalse(b);
    }


}