package com.app.myproject.repo;

import com.app.myproject.entity.UserFillBalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserFillBalanceRepository extends JpaRepository<UserFillBalanceTransaction, UUID> {
}
