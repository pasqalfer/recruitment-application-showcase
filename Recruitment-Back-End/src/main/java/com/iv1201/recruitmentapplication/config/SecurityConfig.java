package com.iv1201.recruitmentapplication.config;

import com.iv1201.recruitmentapplication.security.ApplicantSecurityConfigBean;
import com.iv1201.recruitmentapplication.security.RecruiterSecurityConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = false,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    @Order(1)
    @Configuration
    public static class ApplicantSecurityConfiguration extends WebSecurityConfigurerAdapter {
        private final ApplicantSecurityConfigBean config;

        @Autowired
        public ApplicantSecurityConfiguration(ApplicantSecurityConfigBean config) {
            this.config = config;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests().antMatchers("api/users/applicant/password/reset", "/api/users/applicant/swagger-ui", "/api/users/applicant/register", "/api/users/applicant/authenticate", "/api/users/applicant/password/reset", "/api/users/applicant/password/reset/validateToken","/api/users/applicant/password/reset/updateAccount").permitAll()
                    .and()
                    .antMatcher("/api/users/applicant/**")
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .authenticationProvider(config.getAuthenticationProvider())
                    .addFilterBefore(config.getAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        }
    }


    @Order(2)
    @Configuration
    public static class RecruiterSecurityConfiguration extends WebSecurityConfigurerAdapter {
        private final RecruiterSecurityConfigBean config;

        @Autowired
        public RecruiterSecurityConfiguration(RecruiterSecurityConfigBean config) {
            this.config = config;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests().antMatchers("/api/users/recruiter/swagger-ui", "/api/users/recruiter/register", "/api/users/recruiter/authenticate").permitAll()
                    .and()
                    .antMatcher("/api/users/recruiter/**")
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .authenticationProvider(config.getAuthenticationProvider())
                    .addFilterBefore(config.getAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        }

    }


}
