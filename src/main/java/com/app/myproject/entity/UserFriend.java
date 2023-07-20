package com.app.myproject.entity;


import com.app.myproject.model.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_friend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;


    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private FriendshipStatus status;

    @Column
    private LocalDateTime date;


    @Version
    private Long version;

}
