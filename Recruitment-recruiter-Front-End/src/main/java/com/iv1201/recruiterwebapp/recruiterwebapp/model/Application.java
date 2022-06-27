package com.iv1201.recruiterwebapp.recruiterwebapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * This class represents an application containing the applicants work experience.
 * The application consist of the competences and the applicants available dates to work.
 * The state is set by the recruiters indicating if the application has been approved,
 * denied or not yet handled.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @JsonProperty("id")
    private int id;
    @JsonProperty("version")
    private int version;
    @JsonProperty("state")
    private String state;
    @JsonProperty("competences")
    private List<ApplicationCompetence> competences;
    @JsonProperty("availability")
    private List<AvailabilityPeriod> availability;
    @JsonIgnoreProperties("applicant")
    private Applicant applicant;
}
