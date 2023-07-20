package com.app.myproject.entity;

import com.app.myproject.model.enums.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_command_transaction")
@Getter
@Setter
@NoArgsConstructor
public class UserCommandTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "buyer_balance_id")
    private Balance buyer;

    @ManyToOne
    @JoinColumn(name = "command_id")
    private Command command;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "generic_transaction_id")
    private GenericTransaction transaction;

}
