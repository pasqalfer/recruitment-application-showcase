package com.iv1201.recruitmentapplication.security;

import com.iv1201.recruitmentapplication.entity.Recruiter;
import com.iv1201.recruitmentapplication.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RecruiterDetailsService implements UserDetailsService {
    private final RecruiterRepository recruiterRepository;

    @Autowired
    public RecruiterDetailsService(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Recruiter recruiter = recruiterRepository.findByUsername(username);
        if (Objects.isNull(recruiter))
            throw new UsernameNotFoundException("Username not found");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_RECRUITER"));
        return new ApplicationUser(recruiter.getId(), recruiter.getUsername(), recruiter.getPassword(), authorities);
    }

}
