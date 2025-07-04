package com.example.testjava17.repository.fyna;

import com.example.testjava17.model.entity.fyna.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("otpFynaRepository")
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
}
