package com.aiims.dto.request;

import com.aiims.entity.type.BloodGroupType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdatePatientRequestDto {
    private String name;
    private String gender;
    private String email;
    private LocalDate birthDate;
    private BloodGroupType bloodGroup;
}
