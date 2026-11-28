package kr.swdl.foodingredientmanagement.security;

import kr.swdl.foodingredientmanagement.repository.entitiy.Users;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {
    private final Users users;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + users.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return users.getPassword();
    }

    // 로그인 할 때 사용
    @Override
    public String getUsername() {
        return String.valueOf(users.getUserId());
    }

    // 회원 ID
    public Long getUserId() {
        return users.getUserId();
    }

    // 소속 매장 ID
    public Long getStoreId() {
        return users.getStoreId();
    }

    // 회원 이름
    public String getUserName() {
        return users.getUserName();
    }
}
