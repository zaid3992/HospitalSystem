package com.aiims.mapper;

import com.aiims.dto.request.OnboardDoctorRequestDto;
import com.aiims.dto.response.DoctorResponseDto;
import com.aiims.entity.Doctor;
import com.aiims.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorResponseDto toDto(Doctor doctor);

    @Mapping(target = "id", ignore = true)
    //@Mapping(source = "user", target = "user")
    Doctor toEntity(OnboardDoctorRequestDto onboardDoctorRequestDto, User user);
}
