package com.iv1201.recruiterwebapp.recruiterwebapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * This class represents an applicant, containing account info together with an application to be bound
 * to the applicant.
 * Password is not included for safety issues/no reason to be stored in front end.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Applicant {
    private String username;
    private String email;
    private String pnr;
    private String name;
    private String surname;
    @JsonIgnoreProperties("applicant")
    private Application application;}
