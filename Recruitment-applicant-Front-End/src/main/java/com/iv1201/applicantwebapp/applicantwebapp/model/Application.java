package com.iv1201.applicantwebapp.applicantwebapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Date;
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
    @JsonProperty("state")
    private String state;
    @JsonProperty("competences")
    private List<ApplicationCompetence> competences;
    @JsonIgnoreProperties("availability")
    private List<AvailabilityPeriod> availability;
    @JsonProperty("dateFrom")
    private Date dateFrom;
    @JsonProperty("dateTo")
    private Date dateTo;
}
