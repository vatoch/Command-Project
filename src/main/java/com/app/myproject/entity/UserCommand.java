package com.app.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_command")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "command_id")
    private Command command;


}
