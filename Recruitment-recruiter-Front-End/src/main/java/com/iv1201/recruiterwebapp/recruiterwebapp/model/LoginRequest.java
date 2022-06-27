package com.iv1201.recruiterwebapp.recruiterwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class represents the login elements, username and password,
 * required to authenticate the applicant.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest{
        public String username;
        public String password;
}