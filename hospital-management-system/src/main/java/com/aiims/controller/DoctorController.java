package com.aiims.controller;

import com.aiims.dto.request.DoctorRequestDto;
import com.aiims.dto.response.AppointmentResponseDto;
import com.aiims.dto.response.DoctorResponseDto;
import com.aiims.service.AppointmentService;
import com.aiims.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    @PostMapping("/create")
    public ResponseEntity<DoctorResponseDto> createDoctor(@RequestBody DoctorRequestDto doctorRequestDto) {
        return ResponseEntity.ok(doctorService.createDoctor(doctorRequestDto));
    }

}