package com.iv1201.recruitmentapplication.model.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents an update request of an applicants password.
 * It contains the token corresponding to the applicant and the new password.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateApplicantPasswordRequest {
    private String token;
    private String password;

}