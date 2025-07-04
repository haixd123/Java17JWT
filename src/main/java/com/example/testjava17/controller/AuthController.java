package com.example.testjava17.controller;

import com.example.testjava17.model.entity.fyna.UsersEntity;
import com.example.testjava17.model.request.AuthRequest;
import com.example.testjava17.model.response.BaseResponse;
import com.example.testjava17.repository.fyna.UsersRepository;
import com.example.testjava17.util.ErrorCode;
import com.example.testjava17.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody @Valid AuthRequest request) {
        UsersEntity user = usersRepository.findByUserName(request.getUserName());
        String token = "";
        if (user.getUserName().equals(request.getUserName()) && user.getPassword().equals(request.getPassword())) {
            token = jwtUtil.generateToken(request.getUserName());
        }
        return BaseResponse.success(ErrorCode.SUCCESS, token);
    }
}
