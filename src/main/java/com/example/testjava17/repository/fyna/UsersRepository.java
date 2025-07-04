package com.example.testjava17.repository.fyna;


import com.example.testjava17.model.entity.fyna.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    UsersEntity findByUserName(String userName);
}
