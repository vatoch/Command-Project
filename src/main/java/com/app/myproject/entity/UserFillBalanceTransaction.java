package com.app.myproject.entity;

import com.app.myproject.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_balance_transactiion")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFillBalanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    @ManyToOne
    @JoinColumn(name = "balance_id")
    private Balance balance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private BigDecimal amount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "generic_transaction_id")
    private GenericTransaction transaction;


}
