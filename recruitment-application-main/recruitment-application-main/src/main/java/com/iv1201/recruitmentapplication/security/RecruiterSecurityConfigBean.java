package com.iv1201.recruitmentapplication.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Spring component responsible for providing security configurations for recruiter endpoint
 * */
@Component
@Getter
public class RecruiterSecurityConfigBean {
    private final RecruiterDetailsService recruiterDetailsService;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private AuthTokenFilter authTokenFilter;
    @Value("${applicant.security.jwtSecret}")
    private String authenticationSecret;
    @Value("${applicant.security.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    public RecruiterSecurityConfigBean(RecruiterDetailsService recruiterDetailsService) {
        this.recruiterDetailsService = recruiterDetailsService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.authenticationProvider = new TokenAuthenticationProvider(this.recruiterDetailsService, passwordEncoder);

    }


    @PostConstruct
    public void build() {
        this.authTokenFilter = new AuthTokenFilter.AuthTokenFilterBuilder()
                .authenticationSecret(authenticationSecret)
                .expirationTime(jwtExpirationMs)
                .userDetailsService(recruiterDetailsService)
                .build();
    }

}
