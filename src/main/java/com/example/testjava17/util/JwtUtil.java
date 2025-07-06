package com.example.testjava17.util;

import com.example.testjava17.Exception.JwtValidationException;
import com.example.testjava17.model.entity.fyna.UsersEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your-secret-key-should-be-very-long-and-secure";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("Token đã hết hạn");
        } catch (UnsupportedJwtException e) {
            throw new JwtValidationException("Token không được hỗ trợ");
        } catch (MalformedJwtException e) {
            throw new JwtValidationException("Chữ ký không hợp lệ");
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

    public String generateAccessToken(UsersEntity user) {
        return Jwts.builder()
                .setSubject(user.getUserName())
//                .claim("sessionId", user.getSessionId())
                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 phút
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 1000))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(UsersEntity user) {
        return Jwts.builder()
                .setSubject(user.getUserName())
//                .claim("sessionId", user.getSessionId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)) // 30 ngày
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
}
