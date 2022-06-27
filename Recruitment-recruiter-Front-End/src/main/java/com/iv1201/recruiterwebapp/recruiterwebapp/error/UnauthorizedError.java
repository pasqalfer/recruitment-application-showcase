package com.iv1201.recruiterwebapp.recruiterwebapp.error;

/**
 * This class represents authorization error.
 */
public class UnauthorizedError extends RuntimeException{

    Object user;
    public UnauthorizedError(Object o){
        this.user = o;
    }

    public UnauthorizedError(String msg){
        super(msg);
    }
}
