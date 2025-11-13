package org.example.testifyproject.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Custom UserDetails để chứa thông tin user đầy đủ hơn.
 * Spring Security sẽ dùng class này trong Authentication.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDetails implements UserDetails {

//    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // --- Các cờ kiểm tra trạng thái tài khoản ---
    @Override
    public boolean isAccountNonExpired() {
        return true; // có thể thay bằng logic riêng nếu bạn có cột "expired"
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // hoặc check từ field "status"
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
