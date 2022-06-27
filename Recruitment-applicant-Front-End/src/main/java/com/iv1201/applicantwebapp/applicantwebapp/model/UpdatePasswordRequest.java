package com.iv1201.applicantwebapp.applicantwebapp.model;

import lombok.*;

/**
 * The class represents a get-new-password request, for the user to input the
 * required elements.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordRequest {
    public String token;
    public String password;
}
