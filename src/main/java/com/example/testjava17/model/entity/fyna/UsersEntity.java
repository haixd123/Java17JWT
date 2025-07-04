package com.example.testjava17.model.entity.fyna;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "USERS")
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "PRIVATE_KEY")
    private String privateKey;
}
