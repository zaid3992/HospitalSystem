package com.aiims.controller;

import com.aiims.dto.request.CreateAppointmentRequestDto;
import com.aiims.dto.response.AppointmentResponseDto;
import com.aiims.entity.Appointment;
import com.aiims.mapper.AppointmentMapper;
import com.aiims.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @PostMapping(value = "/create")
    public ResponseEntity<AppointmentResponseDto> createNewAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        AppointmentResponseDto dto = appointmentService.createNewAppointment(createAppointmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{appointmentId}/reassign/{doctorId}")
    public ResponseEntity<AppointmentResponseDto> reassignAppointment(@PathVariable Long appointmentId, @PathVariable Long doctorId) {
        Appointment updated = appointmentService.reAssignAppointmentToAnotherDoctor(appointmentId, doctorId);
        return ResponseEntity.ok(appointmentMapper.toDto(updated));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsOfDoctor() {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsOfDoctor(1L));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsOfDoctor(@PathVariable Long doctorId) {
        List<AppointmentResponseDto> list = appointmentService.getAllAppointmentsOfDoctor(doctorId);
        return ResponseEntity.ok(list);
    }

}
