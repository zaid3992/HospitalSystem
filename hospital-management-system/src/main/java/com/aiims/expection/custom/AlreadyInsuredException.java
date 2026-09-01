package com.aiims.expection.custom;

public class AlreadyInsuredException extends RuntimeException {

    public AlreadyInsuredException(String message) {
        super(message);
    }
}
