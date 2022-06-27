package com.iv1201.recruiterwebapp.recruiterwebapp.model;

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
public class Competence {
    private String competence;
}
