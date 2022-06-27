package com.iv1201.recruitmentapplication.validation;

import com.iv1201.recruitmentapplication.exception.ValidationException;
import com.iv1201.recruitmentapplication.model.payload.request.UserSignupRequest;
import com.iv1201.recruitmentapplication.repository.ApplicantRepository;
import com.iv1201.recruitmentapplication.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Class for validating request for when signing up
 */
@Service
@Qualifier("ApplicantSignupRequestValidator")
public class UserSignupRequestValidator implements Validator<UserSignupRequest> {
    private final ApplicantRepository applicantRepository;

    @Autowired
    public UserSignupRequestValidator(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }

    /**
     * The method to validate the sign up of a user
     * @param userSignupRequest the info the sign up
     * @return The exception for if any field not validated
     */
    @Override
    public ValidationException validate(UserSignupRequest userSignupRequest) {
        ValidationException result = new ValidationException();


        String password = userSignupRequest.password;

        if (ValidationUtil.emptyOrNull(password)) {
            result.addFieldError("password", password, "password can not be empty of blank");
        }

        String name = userSignupRequest.name;
        if (ValidationUtil.emptyOrNull(name)) {
            result.addFieldError("name", name, "name can not be empty or null");
        }

        String surname = userSignupRequest.surname;

        if (ValidationUtil.emptyOrNull(surname)) {
            result.addFieldError("surname", name, "surname can not be empty or null");
        }


        validateUsername(result, userSignupRequest.username);
        validateEmail(result, userSignupRequest.email);

        validatePnr(result, userSignupRequest.pnr);


        if (!result.errors().isEmpty())
            throw result;

        return result;
    }

    /**
     * Method validating the username of a sign up
     * @param result The validation result
     * @param username
     */
    private void validateUsername(ValidationException result, String username) {
        if (ValidationUtil.emptyOrNull(username)) {
            result.addFieldError("username", username, "username can not be empty of blank");
        }

        if (applicantRepository.existsByUsername(username)) {
            result.addFieldError("username", username, "username already exists");
        }
    }

    /**
     * Method validating the email of a sign up
     * @param result The validation result
     * @param email
     */
    private void validateEmail(ValidationException result, String email) {
        if (ValidationUtil.emptyOrNull(email)) {
            result.addFieldError("email", email, "email can not be empty of blank");
        }

        if (!ValidationUtil.isValidEmail(email)) {
            result.addFieldError("email", email, "email is invalid");
        }

        if (applicantRepository.existsByEmail(email)) {
            result.addFieldError("email", email, "email already exists");
        }

    }

    /**
     * Method validating the personal number of a sign up
     * @param result The validation result
     * @param pnr
     */
    private void validatePnr(ValidationException result, String pnr) {
        if (ValidationUtil.emptyOrNull(pnr)) {
            result.addFieldError("pnr", pnr, "pnr can not be empty or null");
        }

        if (applicantRepository.existsByPnr(pnr)) {
            result.addFieldError("pnr", pnr, "pnr already exists");
        }
    }

    @Override
    public boolean supports(Object t) {
        return t instanceof UserSignupRequest;
    }
}
