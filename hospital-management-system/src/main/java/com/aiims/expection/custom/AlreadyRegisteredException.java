package com.aiims.expection.custom;

public class AlreadyRegisteredException extends RuntimeException {

    public AlreadyRegisteredException(String message) {
        super(message);
    }
}
