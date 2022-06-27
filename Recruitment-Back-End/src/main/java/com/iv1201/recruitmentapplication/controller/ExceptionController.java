package com.iv1201.recruitmentapplication.controller;

import com.iv1201.recruitmentapplication.exception.ApplicationUpdateException;
import com.iv1201.recruitmentapplication.exception.CreateApplicationException;
import com.iv1201.recruitmentapplication.exception.ValidationException;
import com.iv1201.recruitmentapplication.model.payload.response.ApiErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Global exception handling controller
 */
@Slf4j
@ControllerAdvice
public class ExceptionController {


    /**
     * Handles form validation exception (missing required parameters)
     * @param ex the exception
     * @return The error response of the exception
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorBody handleError(ValidationException ex) {
        return new ApiErrorBody(ex.getMsg(), ex.errors());
    }


    /**
     * Handles authentication exception (wrong username/password)
     * @param ex the exception
     * @return The error response of the exception
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiErrorBody handleError(AuthenticationException ex) {
        String s = ex.getMessage();
        ValidationException validationException = new ValidationException();
        log.error("Ex: " + ex);
        if (ex instanceof BadCredentialsException) {
            if (s.equals("Wrong password"))
                return new ApiErrorBody("Authentication failed", List.of("Wrong password"));
            else if (s.equals("Null credentials"))
                return new ApiErrorBody("Authentication failed", List.of("Username not found"));
            else
                return new ApiErrorBody("Authentication failed", List.of("Invalid credentials"));
        } else
            return new ApiErrorBody("Authentication failed", null);

    }

    /**
     * Handle exception thrown when a user fails to create an application
     * ex.getCode() indicates if its caused by the client or server.
     * @param ex the exception
     * @return The error response of the exception
     */
    @ExceptionHandler(CreateApplicationException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorBody> handleError(CreateApplicationException ex) {
        ApiErrorBody err = new ApiErrorBody(ex.getMessage(), null);
        log.error("Ex: " + ex);
        if (ex.getCode() == 400) {
            return ResponseEntity.badRequest().body(err);
        } else {
            return ResponseEntity.internalServerError().body(err);
        }
    }

    /**
     * Generalized exception handler that handles uncaught or exceptions of unexcpected type
     * @param ex the exception
     * @return The error response of the exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorBody handleError(Exception ex) {
        log.error("Ex: " + ex);
        ApiErrorBody err = new ApiErrorBody(ex.getMessage(), null);
        ex.printStackTrace();
        return err;
    }

    /**
     * Handles query exception such as wrong SQL statements
     * @param ex the exception
     * @return The error response of the exception
     */
    @ExceptionHandler(QueryException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ApiErrorBody> handleError(QueryException ex) {
        log.error("Ex: " + ex);
        ApiErrorBody err = new ApiErrorBody(ex.getQueryString(), null);
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(err);
    }

    /**
     * Handles exception when updating an application
     * @param ex the exception
     * @return The error response of the exception
     */
    @ExceptionHandler(ApplicationUpdateException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorBody> handleError(ApplicationUpdateException ex) {
        log.error("Ex: " + ex);
        ApiErrorBody err = new ApiErrorBody("Could not update application", ex.getMessage());
        ex.printStackTrace();
        if (ex.getCode() == 400)
            return ResponseEntity.badRequest().body(err);
        else
            return ResponseEntity.internalServerError().body(err);

    }
}
