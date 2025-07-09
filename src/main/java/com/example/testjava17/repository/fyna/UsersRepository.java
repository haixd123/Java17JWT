package com.example.testjava17.repository.fyna;


import com.example.testjava17.model.entity.fyna.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserName(String userName);

//    void deleteBySessionId(String sessionId);
}
