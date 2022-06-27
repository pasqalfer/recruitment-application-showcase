package com.iv1201.recruitmentapplication.model.payload.request;

import com.iv1201.recruitmentapplication.entity.Availability;
import com.iv1201.recruitmentapplication.model.CompetenceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationRequest {
    public List<CompetenceDTO> competences;
    public List<Availability> availability;
    public Date dateFrom;
    public Date dateTo;


}
