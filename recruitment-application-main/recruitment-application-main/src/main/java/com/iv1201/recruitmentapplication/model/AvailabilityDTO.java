package com.iv1201.recruitmentapplication.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvailabilityDTO {

    private Date dateFrom;

    private Date dateTo;


}
