package com.iv1201.recruitmentapplication.security;

import com.iv1201.recruitmentapplication.model.payload.response.JwtResponse;
import com.iv1201.recruitmentapplication.util.JwtUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class SecurityService {
    public abstract JwtResponse usernamePasswordAuthentication(String username, String password);

    protected JwtResponse usernamePasswordAuthentication(String username, String password, AuthenticationProvider authenticationProvider, String authenticationSecret, int jwtExpirationMs) {
        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtUtils.generateJwtToken(authentication, authenticationSecret, jwtExpirationMs);
        ApplicationUser userDetails = (ApplicationUser) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }

}
