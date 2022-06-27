package com.iv1201.recruitmentapplication.security;

import com.iv1201.recruitmentapplication.entity.Applicant;
import com.iv1201.recruitmentapplication.repository.ApplicantRepository;
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
public class ApplicantDetailsService implements UserDetailsService {
    private final ApplicantRepository applicantRepository;

    @Autowired
    public ApplicantDetailsService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Applicant applicant = applicantRepository.findByUsername(username);
        if (Objects.isNull(applicant)) {
            throw new UsernameNotFoundException("Could not find user with name");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_APPLICANT"));
        return new ApplicationUser(applicant.getId(), applicant.getUsername(), applicant.getPassword(), authorities);
    }


}
