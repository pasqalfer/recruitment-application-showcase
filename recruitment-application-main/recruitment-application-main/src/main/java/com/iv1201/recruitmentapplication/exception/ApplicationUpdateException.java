package com.iv1201.recruitmentapplication.exception;

import lombok.Getter;

public class ApplicationUpdateException extends RuntimeException {

    @Getter
    private int code;

    public ApplicationUpdateException() {
        super();
    }

    public ApplicationUpdateException(String msg, int code) {
        super(msg);
        this.code = code;
    }



}
