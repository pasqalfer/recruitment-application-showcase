package com.iv1201.recruitmentapplication.controller;

import com.iv1201.recruitmentapplication.model.ApplicationDTO;
import com.iv1201.recruitmentapplication.model.payload.request.ApplicationStateUpdateRequest;
import com.iv1201.recruitmentapplication.model.payload.request.LoginRequest;
import com.iv1201.recruitmentapplication.model.payload.request.UserSignupRequest;
import com.iv1201.recruitmentapplication.model.payload.response.JwtResponse;
import com.iv1201.recruitmentapplication.security.RecruiterSecurityService;
import com.iv1201.recruitmentapplication.service.ApplicationService;
import com.iv1201.recruitmentapplication.service.RecruiterService;
import com.iv1201.recruitmentapplication.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles the recruiter logic.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users/recruiter")
public class RecruiterController {

    private final RecruiterSecurityService recruiterSecurityService;

    private final RecruiterService recruiterService;

    private final ApplicationService applicationService;

    @Qualifier("UpdateApplicationStateValidator")
    private final Validator<ApplicationStateUpdateRequest> updateStateValidator;

    @Autowired
    public RecruiterController(RecruiterSecurityService recruiterSecurityService, RecruiterService recruiterService, ApplicationService applicationService, Validator<ApplicationStateUpdateRequest> updateStateValidator) {
        this.recruiterSecurityService = recruiterSecurityService;
        this.recruiterService = recruiterService;
        this.applicationService = applicationService;
        this.updateStateValidator = updateStateValidator;
    }

    @GetMapping("/")
    public String home() {
        return "HELLO";
    }

    /**
     * HTTP POST method to authenticate as a user
     * @param loginRequest provides username and credentials for the recruiter trying to authenticate
     * @return response to the request
     */
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody LoginRequest loginRequest) {
        return recruiterSecurityService.usernamePasswordAuthentication(loginRequest.getUsername(), loginRequest.getPassword());
    }

    /**
     * HTTP POST method to create a new user resource
     * @param userSignupRequest create a user of type
     * @return response to the request
     */
    @PostMapping("/register")
    public void authenticate(@RequestBody UserSignupRequest userSignupRequest) {
        recruiterService.register(userSignupRequest);
    }

    /**
     * HTTP GET for getting the application requested
     * @param id The id of the application to find
     * @return the application
     */
    @GetMapping("/applications/{id}/")
    public ApplicationDTO findById(@PathVariable long id) {
        return applicationService.findById(id);
    }

    /**
     * HTTP GET method for returning the list of applications to filter
     * @param pageable The current page
     * @param competence The competence to filter by
     * @return A list of applications by page
     */
    @GetMapping(value = {"/applications/competences/{competence}/page", "/applications/competences/page"})
    public Page<ApplicationDTO> findApplicationsByCompetence(Pageable pageable, @PathVariable(required = false) String[] competence) {
        return applicationService.findByCompetence(pageable, competence);
    }


    /**
     * HTTP POST method for updating state of application
     * @param id The identification of the application
     * @param updateRequest The state to update to
     * @return The application with the new state
     */
    @PostMapping("/applications/{id}/state")
    public ApplicationDTO updateApplicationState(@PathVariable long id, @RequestBody ApplicationStateUpdateRequest updateRequest) {
        updateRequest.setApplicationId(id);
        updateStateValidator.validate(updateRequest);
        return applicationService.updateApplicationState(updateRequest);
    }

}
