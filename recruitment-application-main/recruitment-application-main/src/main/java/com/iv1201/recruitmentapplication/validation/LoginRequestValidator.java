package com.iv1201.recruitmentapplication.validation;

import com.iv1201.recruitmentapplication.exception.ValidationException;
import com.iv1201.recruitmentapplication.model.payload.request.LoginRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Class for validating log in request
 */
@Service
@Qualifier("LoginRequestValidator")
public class LoginRequestValidator implements Validator<LoginRequest> {

    /**
     * The method to validate the log in request
     * @param o the login request info
     * @return The exception for if any field not validated
     */
    @Override
    public ValidationException validate(LoginRequest o) {
        String msg;
        ValidationException result = new ValidationException();
        if (Objects.isNull(o)) {
            msg = "request body is missing";
            result.setError(true);
        } else {
            msg = "invalid username and/or password in login request";
            String username = o.getUsername();

            if (Objects.isNull(username) || username.isEmpty() || username.isBlank() || username.length() == 0) {
                result.addFieldError("username", username, "username can not be empty of blank");
            }


            String password = o.getPassword();
            if (Objects.isNull(password) || password.isEmpty() || password.isBlank() || password.length() == 0) {
                result.addFieldError("password", password, "password can not be empty of blank");
            }
            result.setMsg(msg);
        }

        result.setMsg(msg);
        return result;
    }

    @Override
    public boolean supports(Object o) {
        return o instanceof LoginRequest;
    }
}
