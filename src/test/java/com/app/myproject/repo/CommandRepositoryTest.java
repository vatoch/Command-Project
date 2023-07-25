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
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CommandRepositoryTest {

    @Autowired
    private UserCommandRepository userCommandRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommandRepository commandRepository;

    @Test
    public void getCommandsByUserName_True() {

        Command command1 = Command.builder().name("Deposit money").type(CommandType.PRICED).price(BigDecimal.valueOf(33)).build();
        Command command2 = Command.builder().name("Transfer money").type(CommandType.FREE).price(BigDecimal.valueOf(0)).build();
        User user = User.builder().username("vitali").email("341").phone("332").build();
        userRepository.save(user);
        commandRepository.save(command1);
        commandRepository.save(command2);

        UserCommand userCommand = UserCommand.builder().command(command1).user(user).build();
        UserCommand userCommand1 = UserCommand.builder().command(command2).user(user).build();

        userCommandRepository.save(userCommand);
        userCommandRepository.save(userCommand1);

        List<Command> commands = commandRepository.getCommandsByUsername("vitali", Pageable.ofSize(10)).getContent();
        assertEquals(commands.size(),2);
        assertEquals(commands.get(0).getName(),"Deposit money");
        assertEquals(commands.get(1).getName(),"Transfer money");




    }
    @Test
    public void getCommandsByUserName_False() {
        List<Command> commands = commandRepository.getCommandsByUsername("sito",Pageable.ofSize(10)).getContent();
        assertEquals(commands.size(),0);

    }

    @Test
    public void getAllCommands() {

        Command command = Command.builder().type(CommandType.FREE).price(BigDecimal.valueOf(0)).name("Transfer money").build();
        Command command1 = Command.builder().type(CommandType.PRICED).price(BigDecimal.valueOf(22)).name("Deposit money").build();

        commandRepository.save(command);
        commandRepository.save(command1);
        List<Command> commands = commandRepository.allCommands(Pageable.ofSize(10)).getContent();
        assertEquals(commands.size(),2);
        assertEquals(commands.get(0).getName(),"Transfer money");
        assertEquals(commands.get(1).getName(),"Deposit money");

    }

}