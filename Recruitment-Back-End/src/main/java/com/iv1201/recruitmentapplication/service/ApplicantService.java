package com.iv1201.recruitmentapplication.service;

import com.iv1201.recruitmentapplication.entity.Applicant;
import com.iv1201.recruitmentapplication.entity.PasswordResetToken;
import com.iv1201.recruitmentapplication.exception.NotFoundException;
import com.iv1201.recruitmentapplication.model.ApplicantDTO;
import com.iv1201.recruitmentapplication.model.ApplicationDTO;
import com.iv1201.recruitmentapplication.model.PasswordResetTokenDTO;
import com.iv1201.recruitmentapplication.model.payload.request.ResetApplicantPasswordRequest;
import com.iv1201.recruitmentapplication.model.payload.request.UserSignupRequest;
import com.iv1201.recruitmentapplication.repository.ApplicantRepository;
import com.iv1201.recruitmentapplication.repository.CompetenceRepository;
import com.iv1201.recruitmentapplication.repository.PasswordResetTokenRepository;
import com.iv1201.recruitmentapplication.security.ApplicantSecurityConfigBean;
import com.iv1201.recruitmentapplication.util.DTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Interface to access and perform operations on <code>Applicant</code> resources
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final ApplicantSecurityConfigBean applicantSecurityConfig;

    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository, ApplicantSecurityConfigBean applicantSecurityConfig) {
        this.applicantRepository = applicantRepository;
        this.applicantSecurityConfig = applicantSecurityConfig;
    }

    /**
     * Find applicant by username and return the application associated to it.
     * @param username of the applicant <code>Applicant</code> that owns the fetched application entity
     * @return <code>ApplicationDTO</code> object built from fetched entity
     */
    public ApplicationDTO findApplication(String username) {
        Applicant applicant = applicantRepository.findByUsername(username);
        if (Objects.isNull(applicant)) {
            throw new UsernameNotFoundException("username not found");
        }
        ApplicationDTO applicationDTO = DTOUtil.applicationDTO(applicant);
        return applicationDTO;
    }

    /**
     * Find applicant by username and return the resource.
     * @param username of the applicant <code>Applicant</code> to fetch
     * @return <code>ApplicantDTO</code> object built from fetched entity
     */
    public ApplicantDTO findApplicant(String username) {
        Applicant applicant = applicantRepository.findByUsername(username);
        return DTOUtil.applicantDTO(applicant);
    }

    /**
     * Create a new applicant <code>Applicant</code> resource.
     * @param signUpRequest contains data about the <code>Applicant</code> entity to create,
     *                      should be to avoid errors caused by database constraints
     * @return <code>Boolean</code> if the operation is successful
     */
    public boolean register(UserSignupRequest signUpRequest) {
        Applicant user = Applicant.builder()
                .username(signUpRequest.username)
                .email(signUpRequest.email)
                .name(signUpRequest.name)
                .surname(signUpRequest.surname)
                .password(applicantSecurityConfig.getPasswordEncoder().encode(signUpRequest.password))
                .pnr(signUpRequest.pnr)
                .build();
        applicantRepository.save(user);
        return true;
    }

    /**
     * Resets the password of the applicant.
     * @param applicant The applicant that want to reset their password.
     * @param password The new password to be set.
     * @return If the operation was successful.
     */
    public boolean resetPassword(ApplicantDTO applicant, String password){
        Applicant a = applicantRepository.findByUsername(applicant.getUsername());
        a.setPassword(applicantSecurityConfig.getPasswordEncoder().encode(password));
        applicantRepository.update(a);
        return true;
    }
}
