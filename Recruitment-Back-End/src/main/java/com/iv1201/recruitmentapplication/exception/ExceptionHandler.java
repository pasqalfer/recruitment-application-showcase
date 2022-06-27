package com.iv1201.recruitmentapplication.exception;


import com.iv1201.recruitmentapplication.model.payload.response.ApiErrorBody;
import org.springframework.http.ResponseEntity;

public interface ExceptionHandler<E extends Exception> {
    ResponseEntity<ApiErrorBody> handle(E e);
}
