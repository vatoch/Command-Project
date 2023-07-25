package com.app.myproject.repo;

import com.app.myproject.entity.User;
import com.app.myproject.entity.UserFriend;
import com.app.myproject.model.enums.FriendshipStatus;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

class UserFriendRepositoryTest {

    @Autowired
    private UserFriendRepository userFriendRepository;
    @Autowired
    private UserRepository userRepository;



    @Test
    public void findByUserNames_Correct() {
        User user1 = User.builder().username("vato").phone("5316").email("vc@g,").build();
        User user2 = User.builder().username("levanz").phone("55551111").email("nr").build();
        userRepository.save(user1);
        userRepository.save(user2);
        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.FRIENDS).date(LocalDateTime.now()).build();
        userFriendRepository.save(userFriend);

        Optional<UserFriend> s = userFriendRepository.finDbyUserNames("vato","levanz");
        assertTrue(s.isPresent());
        assertEquals(s.get().getStatus(),FriendshipStatus.FRIENDS);


    }
    @Test
    public void findByUserNames_NotFriends() {
        User user1 = User.builder().username("vato").phone("5316").email("vc@g,").build();
        User user2 = User.builder().username("levanz").phone("55551111").email("nr").build();
        userRepository.save(user1);
        userRepository.save(user2);
        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.DELETED).date(LocalDateTime.now()).build();
        userFriendRepository.save(userFriend);

        Optional<UserFriend> s = userFriendRepository.finDbyUserNames("vato","levanz");
        assertTrue(s.isPresent());
        assertEquals(s.get().getStatus(),FriendshipStatus.DELETED);

    }
    @Test
    public void findByUserNames_Pending() {
        User user1 = User.builder().username("vato").phone("5316").email("vc@g,").build();
        User user2 = User.builder().username("levanz").phone("55551111").email("nr").build();
        userRepository.save(user1);
        userRepository.save(user2);
        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.PENDING).date(LocalDateTime.now()).build();
        userFriendRepository.save(userFriend);

        Optional<UserFriend> s = userFriendRepository.finDbyUserNames("vato","levanz");
        assertTrue(s.isPresent());
        assertEquals(s.get().getStatus(),FriendshipStatus.PENDING);
    }

    @Test
    public void findByUserNames_ReceiverInvalid() {
        User user1 = User.builder().username("vato").phone("5316").email("vc@g,").build();
        userRepository.save(user1);
        Optional<UserFriend> s = userFriendRepository.finDbyUserNames("vato","vzxchh");
        assertFalse(s.isPresent());



    }

    @Test
    public void findByUserNames_SenderInvalid() {
        User user2 = User.builder().username("ch").phone("dddddd").email("zxczxcxzc").build();
        userRepository.save(user2);
        Optional<UserFriend> s = userFriendRepository.finDbyUserNames("cb","ch");
        assertFalse(s.isPresent());
    }
    @Test
    public void findByUserNames_BothInvalid() {
        Optional<UserFriend> s = userFriendRepository.finDbyUserNames("tisakii","volga");
        assertFalse(s.isPresent());
    }

    @Test
    public void getFriends_ValidUserName() {

        User user1 = User.builder().username("pirveli").email("gggg").phone("zxc").build();
        User user2 = User.builder().username("meore").email("mmm").phone("aaa").build();
        User user3 = User.builder().username("mesame").email("zzz").phone("zxcq").build();
        userRepository.saveAll(List.of(user1,user2,user3));
        UserFriend userFriend1 = UserFriend.builder().sender(user1).receiver(user2).status(FriendshipStatus.FRIENDS).date(LocalDateTime.now()).build();
        UserFriend userFriend2 = UserFriend.builder().sender(user1).receiver(user3).status(FriendshipStatus.FRIENDS).date(LocalDateTime.now()).build();
        userFriendRepository.saveAll(List.of(userFriend2,userFriend1));

        List<User> friends = userFriendRepository.getFriends2("pirveli", Pageable.ofSize(10)).getContent();
        assertEquals(friends.size(),3);
        assertEquals(friends.get(0).getUsername(),"pirveli");
        assertEquals(friends.get(1).getUsername(),"meore");




    }

    @Test
    public void getFriends_NotValidUserName() {
        List<User> friends = userFriendRepository.getFriends2("xb",Pageable.ofSize(10)).getContent();
        assertTrue(friends.isEmpty());
    }

    @Test
    public void findByUserNameAndId_Correct() {
        User user1 = User.builder().username("pirveli").email("gggg").phone("zxc").build();
        User user2 = User.builder().username("meore").email("mmm").phone("aaa").build();
        userRepository.save(user1);
        User user2C = userRepository.save(user2);
        UserFriend userFriend = UserFriend.builder().sender(user1).receiver(user2)
                .status(FriendshipStatus.FRIENDS).date(LocalDateTime.now()).build();
        userFriendRepository.save(userFriend);
        Optional<UserFriend> userFriend1 = userFriendRepository.findByUsernameAndId("pirveli",user2C.getId());

        assertTrue(userFriend1.isPresent());


    }
    @Test
    public void findByUserNameAndId_InvalidUserName() {
        User user1 = User.builder().username("meore").email("gggg").phone("zxc").build();
        User user2 = userRepository.save(user1);

        Optional<UserFriend> userFriend = userFriendRepository.findByUsernameAndId("pi",user2.getId());
        assertFalse(userFriend.isPresent());
    }
    @Test
    public void findByUserNameAndId_InvalidId() {
        User user1 = User.builder().username("meore").email("gggg").phone("zxc").build();
        userRepository.save(user1);
        Optional<UserFriend> userFriend = userFriendRepository.findByUsernameAndId("meore", UUID.randomUUID());
        assertFalse(userFriend.isPresent());

    }
    @Test
    public void findByUserNameAndId_BothInvalid() {
        Optional<UserFriend> userFriend = userFriendRepository.findByUsernameAndId("bb",UUID.randomUUID());
        assertFalse(userFriend.isPresent());
    }



}