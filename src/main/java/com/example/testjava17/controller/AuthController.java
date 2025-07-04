package com.example.testjava17.controller;

import com.example.testjava17.model.entity.fyna.UsersEntity;
import com.example.testjava17.model.request.AuthRequest;
import com.example.testjava17.model.response.BaseResponse;
import com.example.testjava17.repository.fyna.UsersRepository;
import com.example.testjava17.util.ErrorCode;
import com.example.testjava17.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody @Valid AuthRequest request, HttpServletResponse response) {
        UsersEntity user = usersRepository.findByUserName(request.getUserName());

        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return BaseResponse.error(ErrorCode.WRONG_INPUT);
        }

        // Cập nhật sessionId
        String sessionId = UUID.randomUUID().toString();
        user.setSessionId(sessionId);
        usersRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(cookie);

        return BaseResponse.success(ErrorCode.SUCCESS, accessToken);
    }

    @PostMapping("/refresh-token")
    public BaseResponse refreshToken(@CookieValue("refreshToken") String refreshToken) {
        try {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            UsersEntity user = usersRepository.findByUserName(username);

            if (user == null || user.getSessionId() == null || !jwtUtil.isTokenValid(refreshToken, user.getSessionId())) {
                return BaseResponse.error(ErrorCode.INVALID_SESSION);
            }

            String newAccessToken = jwtUtil.generateAccessToken(user);
            return BaseResponse.success(ErrorCode.SUCCESS, newAccessToken);
        } catch (JwtException e) {
            return BaseResponse.error(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Lấy refresh token từ cookie
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refreshToken".equals(c.getName())) {
                    refreshToken = c.getValue();
                    break;
                }
            }
        }

        if (refreshToken != null) {
            Claims claims = jwtUtil.extractClaims(refreshToken);
            String sessionId = claims.get("sessionId", String.class);

            // Xoá session khỏi DB
            usersRepository.deleteBySessionId(sessionId);

            // Xoá cookie ở client
            ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
//                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(0) // xoá
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
        }

        return ResponseEntity.ok().build();
    }
}
