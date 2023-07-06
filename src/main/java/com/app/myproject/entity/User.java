package com.app.myproject.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_")
@Getter
@Setter
@NoArgsConstructor
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


    @OneToMany(mappedBy = "user")
    private List<UserCommand> commands;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private Balance balance;

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL)
    private List<UserFriend> friends;
    @Version
    private Long version;

}
