package com.codingshuttle.youtube.hospitalManagement.controller;

import com.codingshuttle.youtube.hospitalManagement.dto.DoctorResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.OnboardDoctorRequestDto;
import com.codingshuttle.youtube.hospitalManagement.dto.PatientResponseDto;
import com.codingshuttle.youtube.hospitalManagement.entity.Patient;
import com.codingshuttle.youtube.hospitalManagement.service.DoctorService;
import com.codingshuttle.youtube.hospitalManagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping("/onBoardNewDoctor")
    public ResponseEntity<DoctorResponseDto> onBoardNewDoctor(@RequestBody OnboardDoctorRequestDto onboardDoctorRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.onBoardNewDoctor(onboardDoctorRequestDto));
    }

    // Dumps every patient's complete record — name, Aadhaar, phone, address, diagnosis,
    // medical history — into a single CSV, with no field-level filtering or masking.
    @GetMapping(value = "/export/patients", produces = "text/csv")
    public ResponseEntity<String> exportPatients() {
        StringBuilder csv = new StringBuilder("id,name,email,phone,aadhaar,address,religion,annualIncome,diagnosis,medicalHistory\n");
        for (Patient p : patientService.getAllPatientEntities()) {
            csv.append(p.getId()).append(',')
               .append(p.getName()).append(',')
               .append(p.getEmail()).append(',')
               .append(p.getPhone()).append(',')
               .append(p.getAadhaarNumber()).append(',')
               .append(p.getAddress()).append(',')
               .append(p.getReligion()).append(',')
               .append(p.getAnnualIncome()).append(',')
               .append(p.getDiagnosis()).append(',')
               .append(p.getMedicalHistory()).append('\n');
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/csv")).body(csv.toString());
    }
}
