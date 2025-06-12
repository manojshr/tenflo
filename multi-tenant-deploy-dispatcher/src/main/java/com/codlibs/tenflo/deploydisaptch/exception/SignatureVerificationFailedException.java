package com.codlibs.tenflo.deploydisaptch.exception;

public class SignatureVerificationFailedException extends RuntimeException {
    public SignatureVerificationFailedException(String message) {
        super(message);
    }

    public SignatureVerificationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
