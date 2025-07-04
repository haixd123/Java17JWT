package com.example.testjava17.config;

import com.example.testjava17.Exception.JwtValidationException;
import com.example.testjava17.model.entity.fyna.UsersEntity;
import com.example.testjava17.model.request.BaseRequest;
import com.example.testjava17.repository.fyna.UsersRepository;
import com.example.testjava17.service.impl.CustomUserDetailsServiceImpl;
import com.example.testjava17.util.Constant;
import com.example.testjava17.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = jwtUtil.getTokenFromRequest(request);
        try {
            // validate token
            if (jwtToken != null && jwtUtil.validateToken(jwtToken)) {
                String username = jwtUtil.getUsernameFromToken(jwtToken);
                UsersEntity user = usersRepository.findByUserName(username);

                if (user != null && jwtUtil.isTokenValid(jwtToken, user.getSessionId()) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JwtValidationException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(Constant.CONTENT_TYPE.APPLICATION_JSON_TYPE);
            response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
