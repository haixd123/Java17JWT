package com.example.testjava17.util;

import com.example.testjava17.Exception.JwtValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your-secret-key-should-be-very-long-and-secure";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
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
            throw new JwtValidationException("Chữ ký không hợp lệ");
        } catch (IllegalArgumentException e) {
            throw new JwtValidationException("Token bị thiếu hoặc rỗng");
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
//                .claim("role", "ADMIN")
//                .claim("userId", 12345L)
                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 10)) // 5s
                .signWith(key)
                .compact();
    }
}
