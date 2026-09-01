package com.aiims.service;

import com.aiims.dto.request.InsuranceRequestDto;
import com.aiims.dto.response.InsuranceResponseDto;
import com.aiims.entity.Insurance;
import com.aiims.entity.Patient;

import com.aiims.expection.custom.InsuranceNotFoundException;
import com.aiims.expection.custom.PatientNotFoundException;
import com.aiims.expection.custom.AlreadyInsuredException;
import com.aiims.repository.InsuranceRepository;
import com.aiims.repository.PatientRepository;
import com.aiims.mapper.InsuranceMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;
    private final InsuranceMapper insuranceMapper;



    @Transactional
    public List<InsuranceResponseDto> getAllInsurances() {
        return insuranceRepository.findAll().stream()
                .map(insuranceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InsuranceResponseDto getInsuranceById(Long id) {
        Insurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new InsuranceNotFoundException("Insurance not found with id: " + id));
        return insuranceMapper.toDto(insurance);
    }

    @Transactional
    public InsuranceResponseDto updateInsurance(Long id, InsuranceRequestDto insuranceRequestDto) {
        Insurance existingInsurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new InsuranceNotFoundException("Insurance not found with id: " + id));

        if (insuranceRequestDto.getPolicyNumber() != null) {
            existingInsurance.setPolicyNumber(insuranceRequestDto.getPolicyNumber());
        }
        if (insuranceRequestDto.getProvider() != null) {
            existingInsurance.setProvider(insuranceRequestDto.getProvider());
        }
        if (insuranceRequestDto.getValidUntil() != null) {
            existingInsurance.setValidUntil(insuranceRequestDto.getValidUntil());
        }

        Insurance updatedInsurance = insuranceRepository.save(existingInsurance);
        return insuranceMapper.toDto(updatedInsurance);
    }

    @Transactional
    public void deleteInsurance(Long id) {
        Insurance insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new InsuranceNotFoundException("Insurance not found with id: " + id));
        insuranceRepository.delete(insurance);
    }

    @Transactional
    public Patient assignInsuranceToPatient(InsuranceRequestDto insuranceRequestDto, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + patientId));

        if (patient.getInsurance() != null) {
            throw new AlreadyInsuredException("Patient already has insurance with id: " + patient.getInsurance().getId());
        }

        patient.setInsurance(insuranceMapper.toEntity(insuranceRequestDto));
        // insurance.setPatient(patient); // bidirectional consistency maintenance

        return patient;
    }

    @Transactional
    public Patient disaccociateInsuranceFromPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));
        patient.setInsurance(null);
        return patient;
    }

    // HW
    //Create three appointment for a patient and then delete Patient

}