package com.app.myproject.entity;
import com.app.myproject.enums.CommandType;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "command")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Command {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommandType type;

    @Column
    @PositiveOrZero
    private BigDecimal price;

    @OneToMany(mappedBy = "command")
    private List<UserCommand> users;

}
