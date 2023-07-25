package com.app.myproject.repo;

import com.app.myproject.entity.UserCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserCommandRepository extends JpaRepository<UserCommand, UUID> {

    @Query("select u from UserCommand u where u.user.username=:username and u.command.id=:commandid")
    Optional<UserCommand> findByUserNameAndId(@Param("username") String username,@Param("commandid") UUID id);

    @Query("select  case when count(u) > 0 then  true else  false end from UserCommand u where u.user.username=:username and u.command.name=:commandname")
    boolean existsByUsernameAndCommandName(@Param("username") String username, @Param("commandname") String commandname);

}
