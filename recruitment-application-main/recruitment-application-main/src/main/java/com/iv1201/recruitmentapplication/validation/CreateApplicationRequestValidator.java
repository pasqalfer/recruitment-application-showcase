package com.iv1201.recruitmentapplication.validation;

import com.iv1201.recruitmentapplication.exception.ValidationException;
import com.iv1201.recruitmentapplication.model.CompetenceDTO;
import com.iv1201.recruitmentapplication.model.payload.request.CreateApplicationRequest;
import com.iv1201.recruitmentapplication.repository.CompetenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

/**
 * Class for validating request for when creating an application
 */
@Service
@Qualifier("CreateApplicationRequestValidator")
public class CreateApplicationRequestValidator implements Validator<CreateApplicationRequest> {
    private final CompetenceRepository competenceRepository;

    @Autowired
    public CreateApplicationRequestValidator(CompetenceRepository competenceRepository) {
        this.competenceRepository = competenceRepository;
    }

    /**
     * Method to validate the application
     * @param createApplicationRequest the application the create
     * @return The exception if any field is incorrect
     */
    @Override
    public ValidationException validate(CreateApplicationRequest createApplicationRequest) {
        ValidationException result = new ValidationException();

        if (Objects.isNull(createApplicationRequest)) {
            result.setMsg("Request body is null");
            throw result;
        }

        if (Objects.isNull(createApplicationRequest.dateFrom)) {
            result.addFieldError("dateFrom", createApplicationRequest.dateFrom, "from date can not be empty or null");
        } else if (createApplicationRequest.dateTo.before(Date.from(Instant.now()))) {
            result.addFieldError("dateFrom", createApplicationRequest.dateFrom, "from date is before today's date");
        }

        if (Objects.isNull(createApplicationRequest.dateTo)) {
            result.addFieldError("dateTo", createApplicationRequest.dateTo, "to date can not be empty or null");
        } else if (createApplicationRequest.dateTo.before(Date.from(Instant.now()))) {
            result.addFieldError("dateTo", createApplicationRequest.dateTo, "to date can not be before today");
        }

        if (Objects.isNull(createApplicationRequest.dateFrom)) {
            result.addFieldError("dateFrom", createApplicationRequest.dateFrom, "from date can not be empty or null");
        }

        Map<String, List<String>> competences = new HashMap<>();
        boolean competenceError = false;
        Set<String> cmp = new HashSet<>();

        for (CompetenceDTO competenceDTO : createApplicationRequest.competences) {
            if (!competences.containsKey(competenceDTO.getCompetence()))
                competences.put(competenceDTO.getCompetence(), new ArrayList<>());

            if (cmp.contains(competenceDTO.getCompetence())) {
                competenceError = true;
                competences.get(competenceDTO.getCompetence()).add("duplicate competence");
            } else
                cmp.add(competenceDTO.getCompetence());

        }

        if (competenceError) {
            result.addFieldError("competences", competences);
        }

        if (!result.errors().isEmpty()) {
            throw result;
        }

        return result;
    }

    @Override
    public boolean supports(Object t) {
        return true;
    }
}
