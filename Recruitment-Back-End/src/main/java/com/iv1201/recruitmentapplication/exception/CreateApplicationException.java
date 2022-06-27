package com.iv1201.recruitmentapplication.exception;

public class CreateApplicationException extends RuntimeException {
    private final int code;

    public CreateApplicationException(String msg, Throwable cause, int code) {
        super(msg, cause);

        this.code = code;
    }

    public CreateApplicationException(String msg) {
        super(msg);
        code = 0;
    }

    public CreateApplicationException(Throwable msg, int code) {
        super(msg);
        this.code = code;
    }

    public CreateApplicationException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
