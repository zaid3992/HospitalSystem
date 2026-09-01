package com.aiims.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InsuranceResponseDto {

    private String id;
    private String policyNumber;
    private String provider;
    private LocalDate validUntil;
}
