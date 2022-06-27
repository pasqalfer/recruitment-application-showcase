package com.iv1201.recruitmentapplication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iv1201.recruitmentapplication.entity.*;
import com.iv1201.recruitmentapplication.model.ApplicationDTO;
import com.iv1201.recruitmentapplication.model.CompetenceDTO;
import com.iv1201.recruitmentapplication.model.payload.request.CreateApplicationRequest;
import com.iv1201.recruitmentapplication.model.payload.request.UserSignupRequest;
import com.iv1201.recruitmentapplication.repository.ApplicationStateRepository;
import com.iv1201.recruitmentapplication.repository.CompetenceRepository;
import com.iv1201.recruitmentapplication.service.ApplicantService;
import com.iv1201.recruitmentapplication.service.ApplicationService;
import com.iv1201.recruitmentapplication.service.RecruiterService;
import com.iv1201.recruitmentapplication.util.DTOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

@Component
public class DatasourceConfig {
    private final CompetenceRepository competenceRepository;
    private final ApplicationStateRepository applicationStateRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;
    private final ApplicantService applicantService;
    private final ApplicationService applicationService;
    private final RecruiterService recruiterService;
    @Value("classpath:/migration/schema/applicants.json")
    private Resource applicantsDataDump;
    @Value("classpath:/migration/schema/recruiters.json")
    private Resource recruitersDataDump;

    public DatasourceConfig(CompetenceRepository competenceRepository, ApplicationStateRepository applicationStateRepository, ApplicantService applicantService, ApplicationService applicationService, RecruiterService recruiterService) {
        this.competenceRepository = competenceRepository;
        this.applicationStateRepository = applicationStateRepository;
        this.applicantService = applicantService;
        this.applicationService = applicationService;
        this.recruiterService = recruiterService;
    }

    @PostConstruct
    private void init() throws IOException {
        if (ddlAuto.equals("create")) {
            List<ApplicationState> stateList = new ArrayList<>();
            stateList.add(ApplicationState.builder().state(ApplicationState.State.UNHANDLED).build());
            stateList.add(ApplicationState.builder().state(ApplicationState.State.ACCEPTED).build());
            stateList.add(ApplicationState.builder().state(ApplicationState.State.REJECTED).build());

            List<Competence> competenceList = new ArrayList<>();
            competenceList.add(Competence.builder().competence("ticket sales").build());
            competenceList.add(Competence.builder().competence("lotteries").build());
            competenceList.add(Competence.builder().competence("roller coaster operation").build());
            competenceRepository.saveAll(competenceList);
            applicationStateRepository.saveAll(stateList);
            migrateRecruiters();
            migrateApplicants();
        }
    }

    public void migrateApplicants() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map> data = mapper.readValue(applicantsDataDump.getFile(), List.class);

        for (Map applicant : data) {
            Map application = (Map) applicant.get("application");

            List<Map<String, Date>> availability1 = DTOUtil.mapDTO(application.get("availability"), List.class);
            List<Availability> availability = new ArrayList<>();
            if (Objects.isNull(availability1))
                availability1 = new ArrayList<>();

            for (Object o : availability1) {
                Availability a = DTOUtil.mapDTO(o, Availability.class);
                availability.add(a);
            }
            List<Map> competences1 = DTOUtil.mapDTO(application.get("competences"), List.class);
            List<CompetenceDTO> competences = new ArrayList<>();

            if (Objects.isNull(competences1))
                competences1 = new ArrayList<>();

            for (Map o : competences1) {
                Double yearsExperience = (Double) o.get("yearsExperience");
                String name = (String) o.get("competence");
                CompetenceDTO competenceDTO = CompetenceDTO.builder()
                        .competence(name)
                        .yearsExperience(yearsExperience)
                        .build();
                competences.add(competenceDTO);
            }

            Applicant applicantEntity = Applicant.builder()
                    .email((String) applicant.get("email"))
                    .username((String) applicant.get("email"))
                    .surname((String) applicant.get("surname"))
                    .name((String) applicant.get("name"))
                    .pnr((String) applicant.get("pnr"))
                    .password("123")
                    .build();


            UserSignupRequest userSignupRequest = DTOUtil.mapDTO(applicantEntity, UserSignupRequest.class);


            applicantService.register(userSignupRequest);

            CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();

            createApplicationRequest.availability = availability;
            createApplicationRequest.competences = competences;
            ApplicationDTO applicationDTO = applicationService.createApplicationFor(userSignupRequest.username, createApplicationRequest);
        }
    }

    public void migrateRecruiters() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map> data = mapper.readValue(recruitersDataDump.getFile(), List.class);

        for (Map recruiter : data) {
            Recruiter recruiterEntity = Recruiter.builder()
                    .username((String) recruiter.get("username"))
                    .surname((String) recruiter.get("surname"))
                    .name((String) recruiter.get("name"))
                    .password((String) recruiter.get("password"))
                    .build();

            UserSignupRequest userSignupRequest = DTOUtil.mapDTO(recruiterEntity, UserSignupRequest.class);

            recruiterService.register(userSignupRequest);
        }
    }
}
