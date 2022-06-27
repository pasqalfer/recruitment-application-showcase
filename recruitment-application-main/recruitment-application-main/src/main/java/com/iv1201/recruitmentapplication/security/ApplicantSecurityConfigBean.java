package com.iv1201.recruitmentapplication.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
/**
 * Spring component responsible for providing security configurations for applicant endpoint
 * */
@Component
@Getter
public class ApplicantSecurityConfigBean {
    private final ApplicantDetailsService applicantDetailsService;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private AuthTokenFilter authTokenFilter;
    @Value("${applicant.security.jwtSecret}")
    private String authenticationSecret;
    @Value("${applicant.security.jwtExpirationMs}")
    private int jwtExpirationMs;


    @Autowired
    public ApplicantSecurityConfigBean(ApplicantDetailsService applicantDetailsService) {
        this.applicantDetailsService = applicantDetailsService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.authenticationProvider = new TokenAuthenticationProvider(applicantDetailsService, passwordEncoder);

    }

    @PostConstruct
    public void build() {
        this.authTokenFilter = new AuthTokenFilter.AuthTokenFilterBuilder()
                .authenticationSecret(authenticationSecret)
                .expirationTime(jwtExpirationMs)
                .userDetailsService(applicantDetailsService)
                .build();
    }


    @Bean
    public List<GrantedAuthority> applicantRoles() {
        return List.of(new SimpleGrantedAuthority("ROLE_APPLICANT"));
    }
}
