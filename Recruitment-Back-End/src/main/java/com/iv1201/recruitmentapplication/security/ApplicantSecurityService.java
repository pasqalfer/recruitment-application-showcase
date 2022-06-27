package com.iv1201.recruitmentapplication.security;

import com.iv1201.recruitmentapplication.model.payload.response.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicantSecurityService extends SecurityService {
    private final ApplicantSecurityConfigBean config;

    @Autowired
    private ApplicantSecurityService(ApplicantSecurityConfigBean config) {
        this.config = config;
    }

    public JwtResponse usernamePasswordAuthentication(String username, String password) {
        return usernamePasswordAuthentication(username, password, config.getAuthenticationProvider(), config.getAuthenticationSecret(), config.getJwtExpirationMs());
    }

}
