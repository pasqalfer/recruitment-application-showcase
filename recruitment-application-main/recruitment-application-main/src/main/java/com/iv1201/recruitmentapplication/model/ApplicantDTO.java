package com.iv1201.recruitmentapplication.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicantDTO {

    private String username;
    private String email;
    private String pnr;
    private String name;
    private String surname;
    @JsonIgnoreProperties("applicant")
    private ApplicationDTO application;

}
