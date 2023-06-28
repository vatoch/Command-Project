package com.app.myproject.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "generic_transaction")
@Getter
@Setter
@NoArgsConstructor
public class GenericTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


}
