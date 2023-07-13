package com.app.myproject.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true,nullable = false,length = 50)
    private String username;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(unique = true)
    private String phone;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<UserCommand> commands;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Balance balance;

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL)
    private List<UserFriend> friends;


}
