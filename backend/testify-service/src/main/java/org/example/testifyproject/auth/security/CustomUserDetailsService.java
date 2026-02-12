package org.example.testifyproject.auth.security;

import lombok.RequiredArgsConstructor;
import org.example.testifyproject.common.constant.UserStatus;
import org.example.testifyproject.entity.User;
import org.example.testifyproject.infrastructure.redis.RedisService;
import org.example.testifyproject.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        boolean nonLocked = user.getUserStatus() != UserStatus.LOCKED;

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

        redisService.setIfAbsent(
                "tokenVersion:" + email,
                1,
                Duration.ofDays(30)
        );

        return new AppUserDetails(
                user.getPasswordHash(),
                user.getEmail(),
                user.getEmailVerified(),
                nonLocked,
                authorities
        );
    }
}

