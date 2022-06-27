package com.iv1201.recruitmentapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CompetenceDTO {
    private String competence;
    private double yearsExperience;
}
