package com.stirlinglms.stirling.entity;

import com.stirlinglms.stirling.entity.user.User;
import com.stirlinglms.stirling.entity.user.grouping.UserType;
import org.springframework.data.annotation.Immutable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Immutable
public final class SpringEntity implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public SpringEntity(User user) {
        this.username = user.getAccountName();
        this.password = user.getPassword();
        this.authorities = Stream.of(UserType.values())
          .filter(t -> t.getLevel() <= user.getType().getLevel())
          .map(t -> new SimpleGrantedAuthority(t.name()))
          .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

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
}
