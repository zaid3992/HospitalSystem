package com.aiims.expection;

import com.aiims.dto.response.ErrorMessage;
import com.aiims.expection.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyRegisteredException.class)
    public ResponseEntity<ErrorMessage> handleAlreadyRegistered(AlreadyRegisteredException ex) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                "ALREADY_REGISTERED",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(AlreadyInsuredException.class)
    public ResponseEntity<ErrorMessage> handleAlreadyInsured(AlreadyInsuredException ex) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                "ALREADY_INSURED",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlePatientNotFound(PatientNotFoundException ex) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                "PATIENT_NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    @ExceptionHandler(InsuranceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleInsuranceNotFound(InsuranceNotFoundException ex) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                "INSURANCE_NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleDoctorNotFound(DoctorNotFoundException ex) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                "DOCTOR_NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

}