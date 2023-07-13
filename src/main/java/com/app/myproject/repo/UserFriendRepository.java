package com.app.myproject.repo;

import com.app.myproject.entity.User;
import com.app.myproject.entity.UserFriend;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend,Long> {


    @Query("""
            SELECT u
            FROM UserFriend u
            WHERE (u.sender.username=:sender 
            AND u.receiver.username=:receiver)
            OR (u.sender.username=:receiver
            AND u.receiver.username=:sender)
            """)
    Optional<UserFriend> finDbyUserNames(@Param("sender") String sender, @Param("receiver") String receiver);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM UserFriend u where (u.sender.username=:username1 and u.receiver.username=:username2) or (u.sender.username=:username2 and u.receiver.username=:username1)")
    boolean existsFriends(@Param("username1") String username1,@Param("username2") String username2);


//    @Query("""
//            SELECT u
//            FROM UserFriend u
//            where u.sender=:username or u.receiver=:username
//            """)
//    @Lock(LockModeType.OPTIMISTIC)
//    Page<UserFriend> getFriends(@Param("username") String username);

    @Query("select u from User u JOIN FETCH UserFriend uf where uf.receiver.username=:username or uf.sender.username=:username")
    Page<User> getFriends2(@Param("username") String username, Pageable pageable);



    @Query("SELECT u FROM UserFriend u where (u.sender.username=:username and u.receiver.id=:id) or (u.receiver.username=:username and u.receiver.id=:id)")
    Optional<UserFriend> findByUsernameAndId(@Param("username") String username , @Param("id") UUID id);




    @Query("SELECT u FROM UserFriend u where u.sender.id=:id and u.receiver.username=:username")
    Optional<UserFriend> findByUsAndId(@Param("username") String username,@Param("id") UUID id);

}
