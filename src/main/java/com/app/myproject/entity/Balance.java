package com.app.myproject.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_balance")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal money;

    @Column
    private LocalDateTime lastUpdated;

    @OneToOne(mappedBy = "balance",fetch = FetchType.EAGER)
    private User user;

}
