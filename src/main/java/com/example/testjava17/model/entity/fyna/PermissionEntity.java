package com.example.testjava17.model.entity.fyna;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PERMISSION")
public class PermissionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;

    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
}
