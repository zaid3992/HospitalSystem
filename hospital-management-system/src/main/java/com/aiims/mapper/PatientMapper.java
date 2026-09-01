package com.aiims.mapper;

import com.aiims.dto.request.CreatePatientRequestDto;
import com.aiims.dto.response.PatientResponseDto;
import com.aiims.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientResponseDto toDto(Patient patient);

    Patient toEntity(CreatePatientRequestDto createPatientRequestDto);
}
