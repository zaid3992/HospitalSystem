package com.aiims.service;

import com.aiims.dto.request.OnboardDoctorRequestDto;
import com.aiims.dto.response.DoctorResponseDto;
import com.aiims.entity.User;
import com.aiims.entity.type.RoleType;
import com.aiims.mapper.DoctorMapper;
import com.aiims.repository.DoctorRepository;
import com.aiims.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;

    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> doctorMapper.toDto(doctor))
                .collect(Collectors.toList());
    }

    public DoctorResponseDto createDoctor(OnboardDoctorRequestDto onboardDoctorRequestDto) {

        User user = userRepository.findById(onboardDoctorRequestDto.getUserId()).orElseThrow();

        if (doctorRepository.existsById(onboardDoctorRequestDto.getUserId())) {
            throw new IllegalArgumentException("Doctor already exists");
        }
        var doctor = doctorMapper.toEntity(onboardDoctorRequestDto,user);
        user.getRoles().add(RoleType.DOCTOR);
        doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

}