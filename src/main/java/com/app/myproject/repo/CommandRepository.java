package com.app.myproject.repo;

import com.app.myproject.entity.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommandRepository extends JpaRepository<Command, UUID> {
    Optional<Command> findByName(String name);

}
