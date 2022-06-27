package com.iv1201.recruiterwebapp.recruiterwebapp.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {
    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String msg;

    @Builder
    public APIException(HttpStatus httpStatus, Throwable cause, String msg) {
        super(msg, cause);
        this.httpStatus = httpStatus;
        this.msg = msg;
    }

    @Builder
    public APIException(HttpStatus httpStatus, Throwable cause, String msg, Object body) {
        super(msg, cause);
        this.httpStatus = httpStatus;
        this.msg = msg;
    }

    @Builder
    public APIException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
        this.msg = msg;
    }

    @Builder
    public APIException(HttpStatus httpStatus, String msg, Object body) {
        super(msg);
        this.httpStatus = httpStatus;
        this.msg = msg;
    }



}