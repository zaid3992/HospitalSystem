package com.aiims.mapper;

import com.aiims.dto.request.DoctorRequestDto;
import com.aiims.dto.response.DoctorResponseDto;
import com.aiims.entity.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorResponseDto toDto(Doctor doctor);

    Doctor toEntity(DoctorRequestDto doctorRequestDto);
}
