package com.app.myproject.repo;

import com.app.myproject.entity.Command;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommandRepository extends JpaRepository<Command, UUID> {
    @Query("select c from Command c join fetch c.users u where u.user.username=:username")
    Page<Command> getCommandsByUsername(@Param("username") String username, Pageable pageable);

    @Query("select c from Command c")
    Page<Command> allCommands(Pageable pageable);

}
