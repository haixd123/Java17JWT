package com.example.testjava17.model.entity.fyna;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "USERS")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS")
    @SequenceGenerator(sequenceName = "SEQ_USERS", allocationSize = 1, name = "SEQ_USERS")

    @Id
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "PASSWORD")
    private String password;

//    @ManyToMany(fetch = FetchType.EAGER) // EAGER luôn load role khi load user
//    @JoinTable(
//            name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<RoleEntity> roles = new HashSet<>();
}
