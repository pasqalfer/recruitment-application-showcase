package com.iv1201.recruitmentapplication.security;

import com.iv1201.recruitmentapplication.util.JwtUtils;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is responsible for decoding and authorizing jwt tokens in incoming requests.
 * An implementation of <code>UserDetailsService</code> is used to decide
 * */
@Builder
class AuthTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final String authenticationSecret;
    private final int expirationTime;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = JwtUtils.parseJwt(request);
            if (JwtUtils.tokenIsValid(jwtToken, authenticationSecret)) {

                String username = JwtUtils.getUsernameFromJwtToken(jwtToken, authenticationSecret);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);

    }


}
