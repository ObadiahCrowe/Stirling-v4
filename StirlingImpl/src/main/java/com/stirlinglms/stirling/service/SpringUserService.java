package com.stirlinglms.stirling.service;

import com.stirlinglms.stirling.entity.SpringEntity;
import com.stirlinglms.stirling.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SpringUserService implements UserDetailsService {

    private final UserService service;
    private final PasswordEncoder encoder;

    @Autowired
    SpringUserService(UserService service, PasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.service.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with the name, " + username + ", was not found!");
        }

        return new SpringEntity(user);
    }
}
