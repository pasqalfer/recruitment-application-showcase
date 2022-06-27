package com.iv1201.recruitmentapplication.service;

import com.iv1201.recruitmentapplication.entity.Applicant;
import com.iv1201.recruitmentapplication.entity.PasswordResetToken;
import com.iv1201.recruitmentapplication.exception.ResetPasswordException;
import com.iv1201.recruitmentapplication.model.PasswordResetTokenDTO;
import com.iv1201.recruitmentapplication.model.payload.request.ResetApplicantPasswordRequest;
import com.iv1201.recruitmentapplication.repository.ApplicantRepository;
import com.iv1201.recruitmentapplication.repository.PasswordResetTokenRepository;
import com.iv1201.recruitmentapplication.security.ApplicantSecurityConfigBean;
import com.iv1201.recruitmentapplication.util.DTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import java.util.UUID;

/**
 * This service is using the repositories to contact the database
 * about reset password requests for the applicant.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class PasswordResetTokenService {
    private final ApplicantRepository applicantRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ApplicantSecurityConfigBean applicantSecurityConfig;

    @Autowired
    public PasswordResetTokenService(ApplicantRepository applicantRepository, PasswordResetTokenRepository passwordResetTokenRepository, ApplicantSecurityConfigBean applicantSecurityConfig) {
        this.applicantRepository = applicantRepository;
        this.passwordResetTokenRepository=passwordResetTokenRepository;
        this.applicantSecurityConfig = applicantSecurityConfig;
    }

    /**
     * Generate a new random password and save it in the database and return to user.
     * Secured with @PostAuthorize
     * @param request Request from user containing email and pnr.
     */
    @PreAuthorize("#username == principal.username AND hasRole('ROLE_APPLICANT')")
    public String createPasswordToken(ResetApplicantPasswordRequest request){
        Applicant applicant = applicantRepository.findByEmail(request.getEmail());
        if (Objects.isNull(applicant)) {
            throw new UsernameNotFoundException("username not found");
        }else if(!applicantSecurityConfig.getPasswordEncoder().matches("123", applicant.getPassword()))
            throw new ResetPasswordException("user already has password");
        String token = UUID.randomUUID().toString();
        PasswordResetToken newToken = PasswordResetToken.builder()
                .token(token)
                .applicant(applicant)
                .build();
        passwordResetTokenRepository.save(newToken);
        return token;
    }

    /**
     * Validate password token, check if it's in the database.
     * @param token The token to be validated.
     * @return The token DTO
     */
    public PasswordResetTokenDTO validatePasswordResetToken(String token) {
        PasswordResetTokenDTO passToken = DTOUtil.passwordResetTokenDTO(passwordResetTokenRepository.findPasswordResetTokenByToken(token));
        if(passToken!=null) {
            return passToken;
        }
        return null;
    }

    /**
     * To delete the token from the database (after user has
     * reset their password, the link with the token should no
     * longer be available).
     * @param token The token to be deleted.
     */
    public void deleteToken(PasswordResetTokenDTO token){
        passwordResetTokenRepository.delete(passwordResetTokenRepository.findPasswordResetTokenByToken(token.getToken()));
    }
}
