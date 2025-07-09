package com.example.testjava17.repository.fyna;


import com.example.testjava17.model.entity.fyna.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, Long> {
    List<UserSessionEntity> findAllBySessionId(String sessionId);
}
