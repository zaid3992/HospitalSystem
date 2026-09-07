package com.aiims.controller;

import com.aiims.dto.request.OnboardDoctorRequestDto;
import com.aiims.dto.response.DoctorResponseDto;
import com.aiims.dto.response.PatientResponseDto;
import com.aiims.service.DoctorService;
import com.aiims.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(patientService.getAllPatients(pageNumber, pageSize));
    }


    @PostMapping("/onboardDoctor")
    public ResponseEntity<DoctorResponseDto> createDoctor(@RequestBody OnboardDoctorRequestDto onboardDoctorRequestDto) {

        return ResponseEntity.ok(doctorService.createDoctor(onboardDoctorRequestDto));
    }
}