package com.codingshuttle.youtube.hospitalManagement.controller;

import com.codingshuttle.youtube.hospitalManagement.entity.Patient;
import com.codingshuttle.youtube.hospitalManagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Internal debugging helpers. This controller is mapped under /debug/**, which
 * WebSecurityConfig leaves open to unauthenticated callers.
 */
@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    private final PatientService patientService;

    // No authentication required. Returns the full patient entity — name, Aadhaar,
    // phone, address, diagnosis and medical history — to anyone who hits the URL.
    @GetMapping("/patient/{id}")
    public ResponseEntity<Patient> debugPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientEntityById(id));
    }
}
