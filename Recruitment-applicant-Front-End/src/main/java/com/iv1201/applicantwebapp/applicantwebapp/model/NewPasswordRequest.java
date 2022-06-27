package com.iv1201.applicantwebapp.applicantwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class represents a get-new-password request, for the user to input the
 * required elements.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordRequest {
    public String email;
    public String pnr;
}
