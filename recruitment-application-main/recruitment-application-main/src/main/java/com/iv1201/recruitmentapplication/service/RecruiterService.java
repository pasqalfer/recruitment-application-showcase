package com.iv1201.recruitmentapplication.service;

import com.iv1201.recruitmentapplication.entity.Recruiter;
import com.iv1201.recruitmentapplication.model.payload.request.UserSignupRequest;
import com.iv1201.recruitmentapplication.repository.RecruiterRepository;
import com.iv1201.recruitmentapplication.security.RecruiterSecurityConfigBean;
import org.springframework.stereotype.Service;

/**
 * Service class used to read and modify <code>Recruiter</code> resources
 */
@Service
public class RecruiterService {
    private final RecruiterRepository applicantRepository;
    private final RecruiterSecurityConfigBean applicantSecurityConfig;

    public RecruiterService(RecruiterRepository applicantRepository, RecruiterSecurityConfigBean applicantSecurityConfig) {
        this.applicantRepository = applicantRepository;
        this.applicantSecurityConfig = applicantSecurityConfig;
    }

    /**
     * Create a new recruiter <code>Recruiter</code> resource.
     * @param signUpRequest contains data about the <code>Recruiter</code> entity to create,
     *                      should be to avoid errors caused by database constraints
     * @return <code>Boolean</code> if the operation is successful
     */
    public boolean register(UserSignupRequest signUpRequest) {
        // Create new user's account
        Recruiter user = Recruiter.builder()
                .username(signUpRequest.username)
                .name(signUpRequest.name)
                .surname(signUpRequest.surname)
                .password(applicantSecurityConfig.getPasswordEncoder().encode(signUpRequest.password))
                .build();
        applicantRepository.save(user);
        return true;
    }
}
