package com.iv1201.applicantwebapp.applicantwebapp.model;

import lombok.*;


/**
 * This class represents the JWT token that is used to represent claims to be transferred
 * between two parties.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtToken {
    private String token;
    private String type = "Bearer";
}