package com.example.testjava17.model.entity.fyna;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "ROLE")
public class RoleEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;

    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    // Quan hệ với UsersEntity (người dùng)
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

    // Quan hệ với Permission
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissions = new HashSet<>();
}
