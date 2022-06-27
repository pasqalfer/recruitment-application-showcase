package com.iv1201.recruitmentapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationDTO {

    private String state;

    private long id;

    private long version;

    private List<CompetenceDTO> competences;

    private List<AvailabilityDTO> availability;

    private Date dateFrom;

    private Date dateTo;

    @JsonIgnoreProperties("application")
    private ApplicantDTO applicant;
}
