package com.iv1201.recruitmentapplication.service;

import com.iv1201.recruitmentapplication.entity.*;
import com.iv1201.recruitmentapplication.exception.ApplicationUpdateException;
import com.iv1201.recruitmentapplication.exception.CreateApplicationException;
import com.iv1201.recruitmentapplication.exception.NotFoundException;
import com.iv1201.recruitmentapplication.model.ApplicationDTO;
import com.iv1201.recruitmentapplication.model.CompetenceDTO;
import com.iv1201.recruitmentapplication.model.payload.request.ApplicationStateUpdateRequest;
import com.iv1201.recruitmentapplication.model.payload.request.CreateApplicationRequest;
import com.iv1201.recruitmentapplication.repository.*;
import com.iv1201.recruitmentapplication.util.DTOUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service class used to read and modify Application objects
 * Operations are protected with method security.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ApplicationService {
    private final CompetenceRepository competenceRepository;
    private final ApplicationStateRepository applicationStateRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicantRepository applicantRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ApplicationCompetenceRepository applicationCompetenceRepository;

    public ApplicationService(CompetenceRepository competenceRepository, ApplicationStateRepository applicationStateRepository, ApplicationRepository applicationRepository, ApplicantRepository applicantRepository, AvailabilityRepository availabilityRepository, ApplicationCompetenceRepository applicationCompetenceRepository) {
        this.competenceRepository = competenceRepository;
        this.applicationStateRepository = applicationStateRepository;
        this.applicationRepository = applicationRepository;
        this.applicantRepository = applicantRepository;
        this.availabilityRepository = availabilityRepository;
        this.applicationCompetenceRepository = applicationCompetenceRepository;
    }

    /**
     * Create a new application <code>Application</code> resource.
     *  1. Fetches user <code>Applicant</code> by <param>username</param>.
     *  2. The created application is associated user.
     * The operation as aborted if the applicant already created an application.
     *
     * @param createApplicationRequest contains data about the application to create
     * @param username username of the applicant to associate the created resource with.
     *                 The username must match the username of the current user principal <code>Principal</code>
     * @return <code>ApplicationDTO</code>
     */
    @PreAuthorize("hasRole('ROLE_APPLICANT') and principal.username == #username")
    public ApplicationDTO createApplicationFor(String username, CreateApplicationRequest createApplicationRequest) {
        try {
            Applicant applicant = applicantRepository.findByUsername(username);
            ApplicationState applicationState = applicationStateRepository.findByState(ApplicationState.State.UNHANDLED);

            if (Objects.isNull(applicationState)) {
                throw new CreateApplicationException("Internal error : application state UNHANDLED could not be found in database", 500);
            }

            Application application = new Application();
            application.setApplicationState(applicationState);
            application.setCompetences(new ArrayList<>());
            List<Availability> availability = createApplicationRequest.availability;


            application = applicationRepository.save(application);

            for (Availability a : availability) {
                a.setApplication(application);
                availabilityRepository.save(a);
            }

            if (Objects.isNull(createApplicationRequest.competences))
                createApplicationRequest.competences = new ArrayList<>();
            for (CompetenceDTO c : createApplicationRequest.competences) {
                Competence competence = competenceRepository.findByCompetence(c.getCompetence());
                if (Objects.isNull(competence)) {
                    // throw custom runtime exception
                    // competence does not exists in database
                    throw new CreateApplicationException("Competence : " + c.getCompetence() + " is not valid", 500);
                }

                boolean duplicateCompetence = application.getCompetences().contains(competence);

                if (duplicateCompetence) {
                    // throw custom runtime exception (client error)
                    // duplicates not allowed in same application
                    throw new CreateApplicationException("Competence : " + c.getCompetence() + " is not valid", 400);
                }

                ApplicationCompetence applicationCompetence = new ApplicationCompetence();
                applicationCompetence.setYearsExperience(c.getYearsExperience());
                applicationCompetence.setCompetence(competence);
                applicationCompetence.setApplication(application);
                application.getCompetences().add(applicationCompetence);
            }

            List<ApplicationCompetence> competenceList = application.getCompetences();
            competenceList = applicationCompetenceRepository.saveAll(competenceList);
            application.setCompetences(competenceList);
            application.setAvailability(availability);
            application = applicationRepository.save(application);
            applicant.setApplication(application);
            applicant = applicantRepository.save(applicant);

            return DTOUtil.applicationDTO(applicant);
        } catch (Exception e) {
            if (e instanceof CreateApplicationException)
                throw e;
            else
                throw new CreateApplicationException("could not create application : ", e, -1);
        }

    }

    /**
     * Update state of an application <code>Application</code> resource if
     * version number in updateRequest <code>ApplicationStateUpdateRequest.version</code> matches
     * the current version of the fetched application <code>Application.version</code>
     *
     * @param updateRequest contains data about the application to update
     * @return
     */
    @PreAuthorize("hasRole('ROLE_RECRUITER')")
    public ApplicationDTO updateApplicationState(ApplicationStateUpdateRequest updateRequest) {
        long id = updateRequest.getApplicationId();
        Application application = applicationRepository.getById(id);

        if (Objects.isNull(application)) {
            throw new ApplicationUpdateException("Could not find application with id=" + id, 400);
        }

        if (application.getVersion() != updateRequest.getVersion()) {
            throw new ApplicationUpdateException("The application has been updated since your last read",  400);
        }

        ApplicationState applicationState = applicationStateRepository.findByState(updateRequest.getNewState());

        if (Objects.isNull(applicationState)) {
            throw new ApplicationUpdateException("Invalid state=" + updateRequest.getNewState().toString(), 500);
        }

        if (applicationState.getState().equals(ApplicationState.State.UNHANDLED)) {
            throw new ApplicationUpdateException("Can not update application state to UNHANDLED",  400);
        }

        application.setApplicationState(applicationState);
        applicationRepository.update(application);
        ApplicationDTO applicationDTO = DTOUtil.applicationDTO(application);
        Applicant applicant = applicantRepository.findByApplication(application);
        applicationDTO.setApplicant(DTOUtil.applicantDTO(applicant));
        return applicationDTO;
    }

    /**
     * Update state of an application <code>Application</code> resource if
     * version number in updateRequest <code>ApplicationStateUpdateRequest.version</code> matches
     * the current version of the fetched application <code>Application.version</code>
     *
     * @param id id of the application to fetch
     * @return
     */
    @PostAuthorize("hasRole('ROLE_RECRUITER') or (hasRole('ROLE_APPLICANT') and returnObject.applicant.username == principal.username)")
    public ApplicationDTO findById(long id) {
        Application application = applicationRepository.getById(id);
        if (Objects.isNull(application)) {
            throw new NotFoundException("Application with id " + id + " was not found");
        }

        ApplicationDTO applicationDTO = DTOUtil.applicationDTO(application);
        Applicant applicant = applicantRepository.findByApplication(application);
        applicationDTO.setApplicant(DTOUtil.applicantDTO(applicant));

        return applicationDTO;
    }

    /**
     * Fetch page of applications.
     * Include all applications that have any of the given competences in <code>competence</code>
     * The results are filtered by competence
     * @param pageable contain page nr <code>Pageable.getPageNumber() </code> and page size<code>Pageable.getPageSize()</code>
     * @param competence array of competences to filter by
     * @return
     */
    @PreAuthorize("hasRole('ROLE_RECRUITER')")
    public Page<ApplicationDTO> findByCompetence(Pageable pageable, String... competence) {
        Page<Application> applications;

        if (Objects.isNull(competence) || competence.length < 1)
            applications = applicationRepository.findAll(pageable);
        else
            applications = applicationRepository.findAllByCompetence(List.of(competence), pageable);

        Page<ApplicationDTO> applicationDTOS = applications.map(application -> {
            ApplicationDTO applicationDTO = DTOUtil.applicationDTO(application);
            Applicant applicant = applicantRepository.findByApplication(application);
            applicationDTO.setApplicant(DTOUtil.applicantDTO(applicant));
            return applicationDTO;
        });


        return applicationDTOS;
    }


}
