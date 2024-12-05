package com.radtimes.rad_times_server.service.authorization;

import com.radtimes.rad_times_server.model.PersonModel;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Integer id;

    private final String username;

    @JsonIgnore
    private String password;

    public UserDetailsImpl(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public static UserDetailsImpl build(PersonModel user) {
        return new UserDetailsImpl(user.getId(), user.getEmail());
    }

    public Integer getId() {
        return id;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
