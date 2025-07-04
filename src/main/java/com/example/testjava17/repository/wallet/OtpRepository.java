package com.example.testjava17.repository.wallet;

import com.example.testjava17.model.entity.wallet.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("otpWalletRepository")
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
}
