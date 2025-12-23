package org.example.testifyproject.security;

import lombok.RequiredArgsConstructor;
import org.example.testifyproject.entity.User;
import org.example.testifyproject.entity.enums.UserStatus;
import org.example.testifyproject.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        boolean enabled = user.getUserStatus() == UserStatus.ACTIVE;

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

        return new AppUserDetails(
                user.getPasswordHash(),
                user.getEmail(),
                enabled,
                authorities
        );
    }
}

