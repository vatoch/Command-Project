package com.app.myproject.repo;

import com.app.myproject.entity.Command;
import com.app.myproject.entity.User;
import com.app.myproject.entity.UserCommand;
import com.app.myproject.model.enums.CommandType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserCommandRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCommandRepository userCommandRepository;
    @Autowired
    private CommandRepository commandRepository;
    @Test
    public void findByUserNameAndId_Correct() {
        User user = User.builder().username("vatoc").phone("321").email("em@gmail.com").build();

        Command command = Command.builder().name("Deposit money").type(CommandType.PRICED).price(BigDecimal.valueOf(3)).build();
        userRepository.save(user);
        Command command1 = commandRepository.save(command);

        UserCommand userCommand = UserCommand.builder().command(command).user(user).build();
        userCommandRepository.save(userCommand);

        Optional<UserCommand> userCommandOptional = userCommandRepository.findByUserNameAndId("vatoc",command1.getId());

        assertTrue(userCommandOptional.isPresent());

    }

    @Test
    public void findByUserNameAndId_UserNameIncorrect() {
        Command command = Command.builder().name("Deposit money").type(CommandType.FREE).price(BigDecimal.valueOf(0)).build();
        Command command1 = commandRepository.save(command);
        Optional<UserCommand> userCommand = userCommandRepository.findByUserNameAndId("zxqqxcc",command1.getId());
        assertFalse(userCommand.isPresent());
    }
    @Test
    public void findByUserNameAndId_CommandIdIncorrect() {
        User user = User.builder().username("votxczc").email("3gtgg@").phone("77777777").build();
        userRepository.save(user);
        Optional<UserCommand> userCommand = userCommandRepository.findByUserNameAndId("votx",UUID.randomUUID());
        assertFalse(userCommand.isPresent());

    }
    @Test
    public void findByUserNameAndId_BothIncorrect() {
        Optional<UserCommand> userCommand = userCommandRepository.findByUserNameAndId("hrqkamd",UUID.randomUUID());
        assertFalse(userCommand.isPresent());
    }


    @Test
    public void existsByUserNameAndCommandName_Correct() {
        User user = User.builder().username("baroni").phone("321111").email("em@gmczxcail.com").build();

        Command command = Command.builder().name("Deposit money").type(CommandType.PRICED).price(BigDecimal.valueOf(3)).build();
        userRepository.save(user);
         commandRepository.save(command);

        UserCommand userCommand = UserCommand.builder().command(command).user(user).build();
        userCommandRepository.save(userCommand);


        boolean userCommand1 = userCommandRepository.existsByUsernameAndCommandName("baroni","Deposit money");
        assertTrue(userCommand1);




    }
    @Test
    public void existsByUserNameAndCommandName_InvalidUserName() {

        Command command = Command.builder().name("Deposit money").type(CommandType.PRICED).price(BigDecimal.valueOf(3)).build();
        commandRepository.save(command);

        boolean userCommand = userCommandRepository.existsByUsernameAndCommandName("nom","Deposit money");
        assertFalse(userCommand);
    }

    @Test
    public void existsByUserNameAndCommandName_InvalidCommand() {
        User user = User.builder().username("baroniq").phone("321111d").email("em@gmczxcail.czom").build();
        userRepository.save(user);
        boolean s = userCommandRepository.existsByUsernameAndCommandName("baroniq","Transfer money");
        assertFalse(s);
    }
    @Test
    public void existsByUserNameAndCommandName_BothInvalid() {
        boolean z = userCommandRepository.existsByUsernameAndCommandName("qito","Vandam");
        assertFalse(z);
    }



}