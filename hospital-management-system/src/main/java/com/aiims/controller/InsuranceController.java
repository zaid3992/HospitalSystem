package com.aiims.controller;

import com.aiims.dto.request.InsuranceRequestDto;
import com.aiims.dto.response.InsuranceResponseDto;
import com.aiims.dto.response.PatientResponseDto;
import com.aiims.entity.Insurance;
import com.aiims.entity.Patient;
import com.aiims.mapper.PatientMapper;
import com.aiims.repository.InsuranceRepository;
import com.aiims.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceRepository insuranceRepository;
    private final InsuranceService insuranceService;
    private final PatientMapper patientMapper;

    @GetMapping("/all")
    public ResponseEntity<List<InsuranceResponseDto>> getAllInsurances() {
        return ResponseEntity.ok(insuranceService.getAllInsurances());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceResponseDto> getInsuranceById(@PathVariable Long id) {
        InsuranceResponseDto insuranceResponseDto = insuranceService.getInsuranceById(id);
        return ResponseEntity.ok(insuranceResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insurance> updateInsurance(@PathVariable Long id, @RequestBody Insurance insuranceDetails) {
        Insurance existing = insuranceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));

        if (insuranceDetails.getPolicyNumber() != null) {
            existing.setPolicyNumber(insuranceDetails.getPolicyNumber());
        }
        if (insuranceDetails.getProvider() != null) {
            existing.setProvider(insuranceDetails.getProvider());
        }
        if (insuranceDetails.getValidUntil() != null) {
            existing.setValidUntil(insuranceDetails.getValidUntil());
        }

        Insurance updated = insuranceRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        if (!insuranceRepository.existsById(id)) {
            throw new EntityNotFoundException("Insurance not found with id: " + id);
        }
        insuranceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assign a new insurance (provided in body) to the patient with given id.
     * The service will maintain bidirectional consistency and persist because of cascade on patient side.
     */
    @PostMapping("/assign/{patientId}")
    public ResponseEntity<PatientResponseDto> assignInsuranceToPatient(@RequestBody InsuranceRequestDto insuranceRequestDto, @PathVariable Long patientId) {
        Patient patient = insuranceService.assignInsuranceToPatient(insuranceRequestDto, patientId);
        PatientResponseDto dto = patientMapper.toDto(patient);
        return ResponseEntity.ok(dto);
    }

    /**
     * Disassociate any insurance from the given patient.
     */
    @PostMapping("/disassociate/{patientId}")
    public ResponseEntity<PatientResponseDto> disassociateInsuranceFromPatient(@PathVariable Long patientId) {
        Patient patient = insuranceService.disaccociateInsuranceFromPatient(patientId);
        PatientResponseDto dto = patientMapper.toDto(patient);
        return ResponseEntity.ok(dto);
    }
}
