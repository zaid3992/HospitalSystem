package com.aiims.expection.custom;

public class DoctorNotFoundException extends  RuntimeException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
