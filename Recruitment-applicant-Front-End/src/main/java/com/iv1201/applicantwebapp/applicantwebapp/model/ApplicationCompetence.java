package com.iv1201.applicantwebapp.applicantwebapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represent one competence an applicant can have.
 * It includes the competence and the years of experience within this competence.
 * This represents one element in the list of competences for an applicant in one application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationCompetence {
    @JsonProperty("competence")
    private String competence;
    @JsonProperty("yearsExperience")
    private double yearsExperience;
    private String from;
    private String to;
    public ApplicationCompetence(String competence, double yearsOfExperience) {
        this.competence = competence;
        this.yearsExperience = yearsOfExperience;
    }
}
