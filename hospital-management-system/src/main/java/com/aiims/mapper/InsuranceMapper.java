package com.aiims.mapper;

import com.aiims.dto.request.InsuranceRequestDto;
import com.aiims.dto.response.InsuranceResponseDto;
import com.aiims.entity.Insurance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InsuranceMapper {

    InsuranceResponseDto toDto(Insurance insurance);

    Insurance toEntity(InsuranceRequestDto insuranceRequestDto);
}
