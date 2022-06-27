package com.iv1201.recruitmentapplication.validation;

import com.iv1201.recruitmentapplication.entity.ApplicationState;
import com.iv1201.recruitmentapplication.exception.ValidationException;
import com.iv1201.recruitmentapplication.model.payload.request.ApplicationStateUpdateRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Class for validating the updating of an application state
 */
@Service
@Qualifier("UpdateApplicationStateValidator")
public class UpdateApplicationStateValidator implements Validator<ApplicationStateUpdateRequest> {

    /**
     * The method to validate the update of an application
     * @param updateRequest the info the update
     * @return The exception for if any field not validated
     */
    @Override
    public ValidationException validate(ApplicationStateUpdateRequest updateRequest) {
        String msg;
        ValidationException result = new ValidationException();
        if (Objects.isNull(updateRequest)) {
            msg = "request body is missing";
            result.setError(true);
        } else {
            msg = "invalid username and/or password in login request";
            ApplicationState.State newState = updateRequest.getNewState();

            if (Objects.isNull(newState)) {
                result.addFieldError("newState", null, "newState can not be empty of blank");
            } else if (newState.equals(ApplicationState.State.UNHANDLED)) {
                result.addFieldError("newState", newState, "application state can not be updated to UNHANDLED");
            }

            result.setMsg(msg);
        }

        if (!result.errors().isEmpty()) {
            throw result;
        }

        result.setMsg(msg);

        return result;
    }

    @Override
    public boolean supports(Object t) {
        return t instanceof ApplicationStateUpdateRequest;
    }
}
