package com.example.testjava17.config;

import com.example.testjava17.model.entity.fyna.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails {
    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return user.getRoles().stream()
//                .flatMap(role -> Stream.concat(
//                        Stream.of(new SimpleGrantedAuthority(role.getName())), // quyền theo Role
//                        role.getPermissions().stream()
//                                .map(p -> new SimpleGrantedAuthority(p.getName())) // quyền theo Permission
//                ))
//                .collect(Collectors.toSet());
//    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // hoặc dùng logic thực tế
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
