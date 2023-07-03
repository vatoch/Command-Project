package com.app.myproject.repo;

import com.app.myproject.entity.UserCommandTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCommandTransactionRepository extends JpaRepository<UserCommandTransaction, UUID> {
}
