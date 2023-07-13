package com.app.myproject.entity;

import com.app.myproject.enums.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_transaction")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal amount;

    @Column(nullable = false)
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Balance sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Balance receiver;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "generic_transaction_id")
    private GenericTransaction transaction;

}
