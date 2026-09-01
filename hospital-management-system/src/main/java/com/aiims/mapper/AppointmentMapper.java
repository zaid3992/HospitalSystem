package com.aiims.mapper;

import com.aiims.dto.response.AppointmentResponseDto;
import com.aiims.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentResponseDto toDto(Appointment appointment);


}
