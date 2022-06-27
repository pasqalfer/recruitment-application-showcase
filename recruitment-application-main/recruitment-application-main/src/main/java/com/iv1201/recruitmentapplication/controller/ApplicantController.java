package com.iv1201.recruitmentapplication.controller;

import com.iv1201.recruitmentapplication.entity.Applicant;
import com.iv1201.recruitmentapplication.entity.Availability;
import com.iv1201.recruitmentapplication.entity.PasswordResetToken;
import com.iv1201.recruitmentapplication.model.ApplicantDTO;
import com.iv1201.recruitmentapplication.model.ApplicationDTO;
import com.iv1201.recruitmentapplication.model.PasswordResetTokenDTO;
import com.iv1201.recruitmentapplication.model.payload.request.*;
import com.iv1201.recruitmentapplication.model.payload.response.JwtResponse;
import com.iv1201.recruitmentapplication.security.ApplicantSecurityService;
import com.iv1201.recruitmentapplication.service.ApplicantService;
import com.iv1201.recruitmentapplication.service.ApplicationService;
import com.iv1201.recruitmentapplication.service.PasswordResetTokenService;
import com.iv1201.recruitmentapplication.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


/**
 * This controller handles the applicant logic.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users/applicant")
public class ApplicantController {
    private final ApplicantSecurityService applicantSecurityService;
    private final ApplicantService applicantService;
    private final ApplicationService applicationService;
    private final PasswordResetTokenService passwordResetTokenService;
    @Qualifier("LoginRequestValidator")
    private final Validator loginRequestValidator;
    @Qualifier("ApplicantSignupRequestValidator")
    private final Validator userSignupRequestValidator;
    @Qualifier("CreateApplicationRequestValidator")
    private final Validator createApplicationRequestValidator;

    @Autowired
    public ApplicantController(ApplicantSecurityService applicantSecurityService, ApplicantService applicantService, ApplicationService applicationService, Validator<LoginRequest> loginRequestValidator, Validator<UserSignupRequest> userSignupRequestValidator, Validator createApplicationRequestValidator, PasswordResetTokenService passwordResetTokenService) {
        this.applicantSecurityService = applicantSecurityService;
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.loginRequestValidator = loginRequestValidator;
        this.userSignupRequestValidator = userSignupRequestValidator;
        this.createApplicationRequestValidator = createApplicationRequestValidator;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    /**
     * HTTP POST method to authenticate as a user
     * @param loginRequest provides username and credentials for the applicant trying to authenticate
     * @return response to the request
     */
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody LoginRequest loginRequest) {
        loginRequestValidator.validate(loginRequest);
        return applicantSecurityService.usernamePasswordAuthentication(loginRequest.getUsername(), loginRequest.getPassword());
    }

    /**
     * HTTP POST method to create a new user resource
     * @param signupRequest create a user of type
     * @return response to the request
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserSignupRequest signupRequest) {
        userSignupRequestValidator.validate(signupRequest);
        boolean success = applicantService.register(signupRequest);
        if (success)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(403).body("username already exists");
    }

    /**
     * HTTP GET method to fetch an applicant resource
     * @param principal The info to identify the applicant
     * @return the applicant resource
     */
    @GetMapping
    public ApplicantDTO applicant(Principal principal) {
        ApplicantDTO applicantDTO = applicantService.findApplicant(principal.getName());
        return applicantDTO;
    }

    /**
     * HTTP POST method that generates a link for the user to use when wanting to reset password.
     * @param requestBody  The new password to reset to
     * @return response to new password
     */
    @PostMapping("/password/reset")
    public ResponseEntity resetPassword(@RequestBody ResetApplicantPasswordRequest requestBody) {
        String token = passwordResetTokenService.createPasswordToken(requestBody);
        if (token != null) {
            //Send email
            System.out.println("http://localhost:8000/updatePassword?token=" + token);
            System.out.println("https://recruiter-app-recruiter.herokuapp.com/updatePassword?token=" + token);
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.status(403).body("username not found");
    }
    /**
     * HTTP POST method that checks if there is token in the database that's the same as the one that
     * the user sent
     * @param token The token that the user sent
     * @return a message from the server
     */
    @PostMapping(value = "/password/reset/validateToken")
    public ResponseEntity validateToken(@RequestBody String token) {
        PasswordResetTokenDTO passToken = passwordResetTokenService.validatePasswordResetToken(token);
        if(passToken != null){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).body("url invalid");
    }

    /**
     * HTTP POST method to update the user that corresponds to the token with a new password.
     * @param updateApplicantPasswordRequest Containing the token and the new password.
     * @return a message from the server
     */
    @PostMapping(value = "/password/reset/updateAccount")
    public ResponseEntity updateAccount(@RequestBody UpdateApplicantPasswordRequest updateApplicantPasswordRequest ) {
        PasswordResetTokenDTO passToken = passwordResetTokenService.validatePasswordResetToken(updateApplicantPasswordRequest.getToken());
        ApplicantDTO applicant = applicantService.findApplicant(passToken.getApplicant().getUsername());
        if(applicant != null) {
            if(applicantService.resetPassword(applicant, updateApplicantPasswordRequest.getPassword()))
                passwordResetTokenService.deleteToken(passToken);
                return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).body("unable to reset password");
    }



    /**
     * HTTP GET method to fetch application owned by applicant
     * @param principal The info to identify the applicant
     * @return the application
     */
    @GetMapping("/application")
    @PostAuthorize("returnObject.applicant.username == principal.name")
    public ApplicationDTO application(Principal principal) {
        return applicantService.findApplication(principal.getName());
    }

    /**
     * HTTP POST method to create an application by an applicant
     * @param principal The info to identify the applicant
     * @param createApplicationRequest The application to create
     * @return The created application
     */
    @PostMapping("/application")
    public ApplicationDTO createApplication(Principal principal, @RequestBody CreateApplicationRequest createApplicationRequest) {
        createApplicationRequestValidator.validate(createApplicationRequest);
        if (createApplicationRequest.availability == null) {
            createApplicationRequest.availability = new ArrayList<>();
            if (createApplicationRequest.dateFrom != null && createApplicationRequest.dateTo != null)
                createApplicationRequest.availability.add(Availability.builder()
                        .dateFrom(createApplicationRequest.dateFrom)
                        .dateTo(createApplicationRequest.dateTo)
                        .build());
        }

        ApplicationDTO applicationDTO = applicationService.createApplicationFor(principal.getName(), createApplicationRequest);
        return applicationDTO;
    }


}
