package com.generateToken.generateToken.exceptionsHandler;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
