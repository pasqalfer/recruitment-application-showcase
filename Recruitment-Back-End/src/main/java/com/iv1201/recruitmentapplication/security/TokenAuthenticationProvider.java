package com.iv1201.recruitmentapplication.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService applicantDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TokenAuthenticationProvider(UserDetailsService applicantDetailsService, PasswordEncoder passwordEncoder) {
        this.applicantDetailsService = applicantDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String credentials = (String) authentication.getCredentials();

        if (Objects.isNull(credentials) || Objects.isNull(username)) {
            throw new BadCredentialsException("Null credentials");
        }

        try {
            UserDetails userDetails = applicantDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(credentials, userDetails.getPassword())) {
                throw new BadCredentialsException("Wrong password");
            } else {
                return new UsernamePasswordAuthenticationToken(userDetails, credentials, userDetails.getAuthorities());
            }
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Null credentials");
        }


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
