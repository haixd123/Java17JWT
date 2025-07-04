package com.example.testjava17.controller;

import com.example.testjava17.model.entity.fyna.OtpEntity;
import com.example.testjava17.model.request.UserRequest;
import com.example.testjava17.repository.fyna.OtpRepository;
import com.example.testjava17.repository.fyna.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class OtpController {

    @Autowired
    @Qualifier("otpWalletRepository")
    private com.example.testjava17.repository.wallet.OtpRepository otpWalletRepository;

    @Autowired
    @Qualifier("otpFynaRepository")
    private OtpRepository otpFynaRepository;

    @PostMapping("/getOtp")
    public ResponseEntity<?> getOtp(@RequestBody UserRequest userRequest) {
        com.example.testjava17.model.entity.wallet.OtpEntity otp = otpWalletRepository.findById(userRequest.getId()).orElse(null);
        OtpEntity otpFyna = otpFynaRepository.findById(userRequest.getId()).orElse(null);
        System.out.println("otp: " + otp);
        System.out.println("otpFyna " + otpFyna);
        return new ResponseEntity<>(otpFyna, HttpStatus.OK);
    }



}
