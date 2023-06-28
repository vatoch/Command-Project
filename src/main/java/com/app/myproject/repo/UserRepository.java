package com.app.myproject.repo;

import com.app.myproject.entity.User;
import com.app.myproject.entity.UserFriend;
import jakarta.persistence.LockModeType;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

//    @Query("SELECT f FROM User u JOIN FETCH u.friends f where u.username=:username")
//    Optional<List<UserFriend>>getFriends(@Param("username") String userName);



    @Query("select u  from   User u where u.id=:id")
    User myQuery(@Param("id") UUID id);



    Optional<User> findByEmail(String email);







}
