package group2.swd392_onlineingredientsystem.security;

import group2.swd392_onlineingredientsystem.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Trả về danh sách quyền của người dùng dưới dạng ROLE_*
     * VD: ROLE_ADMIN, ROLE_USER,...
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() != null && user.getRole().getRoleName() != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Mật khẩu đã được mã hóa
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Các trạng thái tài khoản, có thể thay đổi tùy theo logic hệ thống
    @Override
    public boolean isAccountNonExpired() {
        return true;
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

    /**
     * Trả về thực thể User để sử dụng trong controller hoặc service
     */
    public User getUser() {
        return user;
    }
}
