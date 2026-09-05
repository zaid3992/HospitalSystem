package com.aiims.mapper;

import com.aiims.dto.request.LoginRequestDto;
import com.aiims.dto.response.LoginResponseDto;
import com.aiims.dto.response.SignupResponseDto;
import com.aiims.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(LoginRequestDto dto);

    SignupResponseDto toSignupResponseDto(User user);

    @Mapping(source = "token", target = "jwt")
    @Mapping(source = "user.id", target = "userId")
    LoginResponseDto toLoginResponseDto(User user, String token);
}
