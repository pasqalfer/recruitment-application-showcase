package com.iv1201.recruitmentapplication.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String m) {
        super(m);
    }
}
