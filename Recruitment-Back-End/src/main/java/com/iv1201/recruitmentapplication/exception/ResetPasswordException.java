package com.iv1201.recruitmentapplication.exception;

public class ResetPasswordException extends RuntimeException{
    private final int code;

    public ResetPasswordException(String msg, Throwable cause, int code) {
        super(msg, cause);

        this.code = code;
    }

    public ResetPasswordException(String msg) {
        super(msg);
        code = 0;
    }

    public ResetPasswordException(Throwable msg, int code) {
        super(msg);
        this.code = code;
    }

    public ResetPasswordException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
