package com.aiims.service;
import com.aiims.dto.request.CreatePatientRequestDto;
import com.aiims.dto.request.UpdatePatientRequestDto;
import com.aiims.dto.response.PatientResponseDto;
import com.aiims.entity.Patient;
import com.aiims.entity.Insurance;
import com.aiims.expection.custom.AlreadyRegisteredException;
import com.aiims.expection.custom.PatientNotFoundException;
import com.aiims.mapper.PatientMapper;
import com.aiims.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

//    private final EntityManager entityManager;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponseDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException("Patient Not " +
                "Found with id: " + patientId));
        return patientMapper.toDto(patient);
    }

    @Transactional
    public List<PatientResponseDto> getAllPatients(Integer pageNumber, Integer pageSize) {
        return patientRepository.findAllPatients(PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponseDto createPatient(CreatePatientRequestDto createPatientRequestDto) {
        if (patientRepository.findByNameAndBirthDate(
                createPatientRequestDto.getName(),
                createPatientRequestDto.getBirthDate()
        ).isPresent()) {
            throw new AlreadyRegisteredException("You have already registered. Please login.");
        }

        Patient patient = patientMapper.toEntity(createPatientRequestDto);

        // Detach any mapped Insurance before persisting the Patient to avoid transient reference issues
        Insurance insurance = patient.getInsurance();
        if (insurance != null) {
            patient.setInsurance(null);
        }

        Patient savedPatient = patientRepository.save(patient);

        // Attach and persist insurance (if present) so it references the persisted patient
        savedPatient = attachInsuranceAndSave(savedPatient, insurance);

        return patientMapper.toDto(savedPatient);
    }

    private Patient attachInsuranceAndSave(Patient patient, Insurance insurance) {
        if (insurance == null) {
            return patient;
        }
        insurance.setPatient(patient);
        patient.setInsurance(insurance);
        return patientRepository.save(patient);
    }

    @Transactional
    public PatientResponseDto updatePatient(Long patientId, UpdatePatientRequestDto updatePatientRequestDto) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient Not Found with id: " + patientId));

        if (updatePatientRequestDto.getName() != null) {
            existingPatient.setName(updatePatientRequestDto.getName());
        }
        if (updatePatientRequestDto.getEmail() != null) {
            existingPatient.setEmail(updatePatientRequestDto.getEmail());
        }
        if (updatePatientRequestDto.getGender() != null) {
            existingPatient.setGender(updatePatientRequestDto.getGender());
        }
        if (updatePatientRequestDto.getBirthDate() != null) {
            existingPatient.setBirthDate(updatePatientRequestDto.getBirthDate());
        }
        if (updatePatientRequestDto.getBloodGroup() != null) {
            existingPatient.setBloodGroup(updatePatientRequestDto.getBloodGroup());
        }

        Patient updatedPatient = patientRepository.save(existingPatient);
        return patientMapper.toDto(updatedPatient);
    }

    @Transactional
    public void deletePatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient Not Found with id: " + patientId);
        }
        patientRepository.deleteById(patientId);
    }

}