package com.iv1201.recruitmentapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class represents an DTO of the password reset token entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordResetTokenDTO {
    String token;

    @JsonIgnoreProperties("applicant")
    ApplicantDTO applicant;
}
