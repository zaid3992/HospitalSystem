package com.aiims.service;

import com.aiims.dto.request.DoctorRequestDto;
import com.aiims.dto.response.AppointmentResponseDto;
import com.aiims.dto.response.DoctorResponseDto;
import com.aiims.entity.Doctor;
import com.aiims.mapper.AppointmentMapper;
import com.aiims.mapper.DoctorMapper;
import com.aiims.repository.DoctorRepository;
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
    private final AppointmentMapper appointmentMapper;

    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> doctorMapper.toDto(doctor))
                .collect(Collectors.toList());
    }

    public DoctorResponseDto createDoctor(DoctorRequestDto doctorRequestDto) {
        var doctor = doctorMapper.toEntity(doctorRequestDto);
        doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

}