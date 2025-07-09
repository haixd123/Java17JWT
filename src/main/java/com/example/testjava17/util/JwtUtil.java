package com.example.testjava17.util;

import com.example.testjava17.Exception.JwtValidationException;
import com.example.testjava17.model.entity.fyna.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String jwtSecretKeyKey;

    @Autowired
    private AesUtil aesUtil;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecretKeyKey.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("Token đã hết hạn");
        } catch (UnsupportedJwtException e) {
            throw new JwtValidationException("Token không được hỗ trợ");
        } catch (MalformedJwtException e) {
            throw new JwtValidationException("Token không hợp lệ");
        } catch (IllegalArgumentException e) {
            throw new JwtValidationException("Token bị thiếu hoặc rỗng");
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public String generateAccessToken(String userName, String sessionId) throws Exception {
        String jwt = Jwts.builder()
                .setSubject(userName)
                .claim("sessionId", sessionId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .signWith(key)
                .compact();

        return aesUtil.encrypt(jwt);
    }

    public String generateRefreshToken(String userName, String sessionId) {
        return Jwts.builder()
                .setSubject(userName)
                .claim("sessionId", sessionId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15L * 24 * 60 * 60 * 1000)) // 30 ngày
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, String sessionIdFromDB) {
        Claims claims = extractClaims(token);
        String sessionIdFromToken = claims.get("sessionId", String.class);
        return sessionIdFromToken != null && sessionIdFromToken.equals(sessionIdFromDB);
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }


    public String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
