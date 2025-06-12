package com.codlibs.tenflo.deploydisaptch.exception;

public class AwsException extends RuntimeException {
    public AwsException(String message) {
        super(message);
    }

    public AwsException(String message, Throwable cause) {
        super(message, cause);
    }
}
