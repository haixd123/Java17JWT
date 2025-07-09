package com.example.testjava17.config;

import com.example.testjava17.Exception.BusinessException;
import com.example.testjava17.Exception.JwtValidationException;
import com.example.testjava17.model.entity.fyna.UserSessionEntity;
import com.example.testjava17.model.entity.fyna.UserEntity;
import com.example.testjava17.repository.fyna.UserSessionRepository;
import com.example.testjava17.repository.fyna.UsersRepository;
import com.example.testjava17.service.impl.CustomUserDetailsServiceImpl;
import com.example.testjava17.util.AesUtil;
import com.example.testjava17.util.Constant;
import com.example.testjava17.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private AesUtil aesUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.getTokenFromRequest(request);
        String jwtToken = null;
        try {
            if (token != null) {
                try {
                    jwtToken = aesUtil.decrypt(token);
                } catch (Exception e) {
                    throw new JwtValidationException(e.getMessage());
                }
                if (jwtUtil.validateToken(jwtToken)) {
                    String username = jwtUtil.getUsernameFromToken(jwtToken);
                    UserEntity user = usersRepository.findByUserName(username);

                    Claims claims = jwtUtil.extractClaims(jwtToken);
                    String sessionId = claims.get("sessionId", String.class);
                    log.info("JwtFilter | doFilterInternal | sessionId: " + sessionId);

                    // can  check sessionId moi nhat
                    List<UserSessionEntity> userSession = userSessionRepository.findAllBySessionId(sessionId);
                    log.info("AuthController | refreshToken | userSession: " + userSession);
                    UserSessionEntity userSessionEntity = userSession.get(0);
                    LocalDateTime time = LocalDateTime.now();

                    if (user != null && userSessionEntity.getSessionId() != null && time.isBefore(userSessionEntity.getExpiresAt()) && jwtUtil.isTokenValid(jwtToken, userSessionEntity.getSessionId()) &&
                            SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            }
            // validate token
        } catch (JwtValidationException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(Constant.CONTENT_TYPE.APPLICATION_JSON_TYPE);
            response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
