package com.codingshuttle.youtube.hospitalManagement.service;

import com.codingshuttle.youtube.hospitalManagement.entity.Patient;
import com.codingshuttle.youtube.hospitalManagement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final PatientRepository patientRepository;

    @Value("${sms.gateway.apiKey}")
    private String smsApiKey;

    /**
     * Sends an appointment-reminder SMS to the patient. The patient's
     * consentWithdrawn flag is never checked, so patients who explicitly asked to
     * stop being contacted keep receiving messages.
     */
    public void sendAppointmentReminder(Long patientId, String message) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + patientId));

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.msg91.com/api/v5/flow/"))
                    .header("authkey", smsApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"mobile\":\"" + patient.getPhone() + "\",\"message\":\"" + message + "\"}"))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error("Failed to send reminder to {}", patient.getPhone(), e);
        }
    }
}
