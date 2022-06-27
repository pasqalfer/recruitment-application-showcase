package com.iv1201.recruiterwebapp.recruiterwebapp.error;

/**
 * This class represents error when fetching applications.
 */
public class FetchApplicationException extends RuntimeException{

    public FetchApplicationException(String message, Throwable cause){
        super(message,cause);
    }
}
