package com.example.testjava17.controller;

import com.example.testjava17.model.entity.fyna.UserSessionEntity;
import com.example.testjava17.model.entity.fyna.UserEntity;
import com.example.testjava17.model.request.AuthRequest;
import com.example.testjava17.model.response.BaseResponse;
import com.example.testjava17.repository.fyna.UserSessionRepository;
import com.example.testjava17.repository.fyna.UsersRepository;
import com.example.testjava17.util.AesUtil;
import com.example.testjava17.util.ErrorCode;
import com.example.testjava17.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AesUtil aesUtil;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;


    @Value("${secureCode.secret.key}")
    private String secureCodeSecretKey;

    @PostMapping("/login")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BaseResponse login(HttpServletRequest httpRequest, @RequestBody AuthRequest request, HttpServletResponse response) throws Exception {
        UserEntity user = usersRepository.findByUserName(request.getUserName());

        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return BaseResponse.error(ErrorCode.WRONG_INPUT);
        }

        String sessionId = UUID.randomUUID().toString();
        String deviceId = httpRequest.getHeader("device-id");
        String ip = jwtUtil.getClientIp(httpRequest);

        // Lưu vào bảng user_device hoặc user_session
        UserSessionEntity userSession = new UserSessionEntity();
        userSession.setUserId(user.getId());
//        userSession.setDeviceId(deviceId);
        userSession.setSessionId(sessionId);
        userSession.setIpAddress(ip);
        userSession.setCreatedAt(LocalDateTime.now());
        userSession.setExpiresAt(LocalDateTime.now().plusDays(15)); // 15 ngày

        userSessionRepository.save(userSession);

        String accessToken = jwtUtil.generateAccessToken(request.getUserName(), sessionId);
        String refreshToken = jwtUtil.generateRefreshToken(request.getUserName(), sessionId);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(15 * 24 * 60 * 60);
        response.addCookie(cookie);

        var secureCode = user.getUserName() + sessionId + "|" + secureCodeSecretKey;
        var authToken = aesUtil.encrypt(secureCode + "#" + accessToken);
        Map<String, String> ltData = new HashMap<>();
        ltData.put("accessToken", accessToken);
        ltData.put("authToken", authToken);

        return BaseResponse.success(ErrorCode.SUCCESS, ltData);
    }

    @PostMapping("/refresh-token")
    public BaseResponse refreshToken(@CookieValue("refreshToken") String refreshToken) {
        try {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            UserEntity user = usersRepository.findByUserName(username);

            Claims claims = jwtUtil.extractClaims(refreshToken);
            String sessionId = claims.get("sessionId", String.class);
            log.info("AuthController | refreshToken | sessionId: " + sessionId);
            // can  check sessionId moi nhat
            List<UserSessionEntity> userSession = userSessionRepository.findAllBySessionId(sessionId);
            log.info("AuthController | refreshToken | userSession: " + userSession);
            UserSessionEntity userSessionEntity = userSession.get(0);
            LocalDateTime time = LocalDateTime.now();

            if (!userSession.isEmpty()) {
                if (user == null || userSessionEntity.getSessionId() == null || !jwtUtil.isTokenValid(refreshToken, userSessionEntity.getSessionId()) || userSessionEntity.getExpiresAt().isBefore(time)) {
                    return BaseResponse.error(ErrorCode.INVALID_SESSION);
                }
                String sessionIdNew = UUID.randomUUID().toString();
                userSessionEntity.setSessionId(sessionIdNew);
                userSessionRepository.save(userSessionEntity);

                String newAccessToken = jwtUtil.generateAccessToken(username, sessionIdNew);
                log.info("AuthController | refreshToken | newAccessToken: " + newAccessToken);
                return BaseResponse.success(ErrorCode.SUCCESS, newAccessToken);
            }
            return BaseResponse.error(ErrorCode.PARTNER_NOT_FOUND);
        } catch (JwtException e) {
            return BaseResponse.error(ErrorCode.INVALID_REFRESH_TOKEN);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

//            log.info("AuthController | logout | sessionId: " + sessionId);
//            List<userSessionEntity> userSession = userSessionRepository.findAllBySessionId(sessionId);
//            if(!userSession.isEmpty()) {
//                userSessionEntity userSessionEntity = userSession.get(0);
//            }
//
            // Xoá session khỏi DB
//            usersRepository.deleteBySessionId(sessionId);

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
