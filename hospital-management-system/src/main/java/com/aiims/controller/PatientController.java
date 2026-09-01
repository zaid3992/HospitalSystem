package com.aiims.controller;

import com.aiims.dto.request.CreateAppointmentRequestDto;
import com.aiims.dto.request.CreatePatientRequestDto;
import com.aiims.dto.request.UpdatePatientRequestDto;
import com.aiims.dto.response.AppointmentResponseDto;
import com.aiims.dto.response.PatientResponseDto;
import com.aiims.service.AppointmentService;
import com.aiims.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping(value = "/appointments")
    public ResponseEntity<AppointmentResponseDto> createNewAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createNewAppointment(createAppointmentRequestDto));
    }

    @GetMapping("/profile")
    public ResponseEntity<PatientResponseDto> getPatientProfile() {
        Long patientId = 4L;
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

    /**
     * Endpoint to create a new patient.
     *
     * @param createPatientRequestDto the request body containing patient details
     * @return a ResponseEntity containing the created PatientResponseDto
     */
    @PostMapping(value = "/create")
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody CreatePatientRequestDto createPatientRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(createPatientRequestDto));
    }

    /**
     * Endpoint to retrieve a patient's profile by their ID.
     *
     * @param patientId the ID of the patient
     * @return a ResponseEntity containing the PatientResponseDto
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> getPatientProfileById(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }

    /**
     * Endpoint to retrieve a paginated list of patients.
     *
     * @param pageNumber the page number to retrieve (default is 0)
     * @param pageSize   the number of patients per page (default is 10)
     * @return a ResponseEntity containing a list of PatientResponseDto
     */
    @GetMapping()
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(patientService.getAllPatients(pageNumber, pageSize));
    }
    /**
     * Endpoint to update a patient's profile by their ID.
     *
     * @param patientId                the ID of the patient to update
     * @param updatePatientRequestDto  the request body containing updated patient details
     * @return a ResponseEntity containing the updated PatientResponseDto
     */
    @PutMapping("/update/{patientId}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable Long patientId, @RequestBody UpdatePatientRequestDto updatePatientRequestDto) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, updatePatientRequestDto));
    }

    /**
     * Endpoint to delete a patient by their ID.
     *
     * @param patientId the ID of the patient to delete
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }

}