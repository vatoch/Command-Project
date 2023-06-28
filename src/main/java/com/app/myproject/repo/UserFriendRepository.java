package com.app.myproject.repo;

import com.app.myproject.entity.User;
import com.app.myproject.entity.UserFriend;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend,Long> {


    @Query("SELECT u FROM UserFriend u WHERE u.sender=:sender and u.receiver=:receiver")
    @Lock(LockModeType.OPTIMISTIC)
    Optional<UserFriend> finDbyUserNames(@Param("sender") String sender, @Param("receiver") String receiver);

}
