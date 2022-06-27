package com.iv1201.recruitmentapplication.controller;

import com.iv1201.recruitmentapplication.entity.Competence;
import com.iv1201.recruitmentapplication.repository.CompetenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller handles the logic of competences in an application.
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/competences")
public class CompetenceController {
    private final CompetenceRepository competenceRepository;

    @Autowired
    public CompetenceController(CompetenceRepository competenceRepository) {
        this.competenceRepository = competenceRepository;
    }

    /**
     * HTTP GET method for getting a compatence
     * @param type The competence
     * @return The competence
     */
    @GetMapping("/{type}/")
    @PreAuthorize("hasAnyRole('ROLE_APPLICANT', 'ROLE_RECRUITER')")
    public Competence competences(@PathVariable String type) {
        return competenceRepository.findByCompetence(type);
    }

    /**
     * HTTP GET method to get all competences
     * @return a list oc competences
     */
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_APPLICANT', 'ROLE_RECRUITER')")
    public List<Competence> competences() {
        return competenceRepository.findAll();
    }

}
