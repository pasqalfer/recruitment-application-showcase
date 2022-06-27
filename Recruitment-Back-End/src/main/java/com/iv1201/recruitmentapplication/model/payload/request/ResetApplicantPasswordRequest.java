package com.iv1201.recruitmentapplication.model.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a reset applicant password request.
 * The applicant will send in email and pnr for authorization
 * in order to be able to reset password.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetApplicantPasswordRequest {
    private String email;
    private String pnr;

}
