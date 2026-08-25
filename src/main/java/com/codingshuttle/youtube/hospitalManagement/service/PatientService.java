package com.codingshuttle.youtube.hospitalManagement.service;

import com.codingshuttle.youtube.hospitalManagement.dto.PatientResponseDto;
import com.codingshuttle.youtube.hospitalManagement.entity.Patient;
import com.codingshuttle.youtube.hospitalManagement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PatientResponseDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient Not " +
                "Found with id: " + patientId));
        return modelMapper.map(patient, PatientResponseDto.class);
    }

    // Returns the full patient entity, including Aadhaar, diagnosis and medical history.
    // Callers do not check whether the requester owns this record.
    @Transactional
    public Patient getPatientEntityById(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient Not " +
                "Found with id: " + patientId));
        // Full patient record (name, Aadhaar, diagnosis, contact details) written to the application log.
        log.info("Fetched patient record: {}", patient);
        return patient;
    }

    // Looks up patients by Aadhaar number and phone, both of which arrive as URL query parameters.
    @Transactional
    public List<Patient> searchByAadhaarAndPhone(String aadhaarNumber, String phone) {
        log.info("Searching patients with aadhaar={} phone={}", aadhaarNumber, phone);
        return patientRepository.findAll()
                .stream()
                .filter(p -> aadhaarNumber == null || aadhaarNumber.equals(p.getAadhaarNumber()))
                .filter(p -> phone == null || phone.equals(p.getPhone()))
                .collect(Collectors.toList());
    }

    // "Deletes" a patient by flipping a flag. The row and every field of PII are kept forever.
    @Transactional
    public void deletePatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient Not " +
                "Found with id: " + patientId));
        patient.setIsDeleted(true);
        patientRepository.save(patient);
    }

    public List<PatientResponseDto> getAllPatients(Integer pageNumber, Integer pageSize) {
        return patientRepository.findAllPatients(PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(patient -> modelMapper.map(patient, PatientResponseDto.class))
                .collect(Collectors.toList());
    }

    // Exposes the raw entity list for CSV export, all columns included.
    public List<Patient> getAllPatientEntities() {
        return patientRepository.findAll();
    }
}
