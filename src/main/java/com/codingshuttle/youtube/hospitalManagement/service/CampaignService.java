package com.codingshuttle.youtube.hospitalManagement.service;

import com.codingshuttle.youtube.hospitalManagement.entity.Patient;
import com.codingshuttle.youtube.hospitalManagement.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignService {

    private final PatientRepository patientRepository;

    /**
     * Patient email addresses were collected so the hospital could send appointment
     * reminders and clinical communication. Here they are reused for an unrelated
     * purpose — a marketing promotion — and sent to every patient without checking
     * marketingConsent or consentWithdrawn.
     */
    public void sendHealthCampPromo(String promoSubject, String promoBody) {
        List<Patient> patients = patientRepository.findAll();
        for (Patient p : patients) {
            log.info("Sending health-camp promo to {}", p.getEmail());
            deliverPromoEmail(p.getEmail(), promoSubject, promoBody);
        }
    }

    private void deliverPromoEmail(String email, String subject, String body) {
        // hands off to the transactional email provider used for appointment reminders
    }
}
