package com.iv1201.recruiterwebapp.recruiterwebapp.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The class represents a register request, for the user to input the
 * required elements.
 */
@Getter
@Setter
public class RegisterRequest {
    public String username;
    public String email;
    public String pnr;
    public String password;
    public String name;
    public String surname;
}
