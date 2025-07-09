package com.example.testjava17.model.entity.fyna;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "USER_SESSION")
public class UserSessionEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_SESSION")
    @SequenceGenerator(sequenceName = "SEQ_USER_SESSION", allocationSize = 1, name = "SEQ_USER_SESSION")

    @Id
    private Long id;
    @Column(name = "USER_ID")
    private Long userId;
//    @Column(name = "DEVICE_ID")
//    private String deviceId;
    @Column(name = "SESSION_ID")
    private String sessionId;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @Column(name = "EXPIRES_AT")
    private LocalDateTime expiresAt;

}
