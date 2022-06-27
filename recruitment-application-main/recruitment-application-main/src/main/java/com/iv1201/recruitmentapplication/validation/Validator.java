package com.iv1201.recruitmentapplication.validation;

import com.iv1201.recruitmentapplication.exception.ValidationException;
import org.springframework.stereotype.Component;

/**
 * Interface for validating incoming
 **/
@Component
public interface Validator<T> {
    ValidationException validate(T t);

    boolean supports(Object t);
}
