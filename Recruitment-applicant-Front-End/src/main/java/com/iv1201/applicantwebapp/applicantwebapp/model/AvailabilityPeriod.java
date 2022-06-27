package com.iv1201.applicantwebapp.applicantwebapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Date;

/**
 * This class represents availability periods for an applicant.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityPeriod {
    @JsonProperty("dateFrom")
    private Date dateFrom;
    @JsonProperty("dateTo")
    private Date dateTo;
}
