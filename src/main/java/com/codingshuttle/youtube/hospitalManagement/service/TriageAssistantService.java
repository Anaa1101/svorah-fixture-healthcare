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
public class TriageAssistantService {

    private final PatientRepository patientRepository;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    /**
     * Builds an LLM prompt containing the patient's real name, Aadhaar, date of birth
     * and full medical history, and sends it to a third-party API (OpenAI) with no
     * redaction, de-identification or data-processing safeguard.
     */
    public String suggestTriage(Long patientId, String symptoms) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found: " + patientId));

        String prompt = "Patient " + patient.getName()
                + " (Aadhaar " + patient.getAadhaarNumber()
                + ", DOB " + patient.getBirthDate()
                + ", known history: " + patient.getMedicalHistory() + ")"
                + " reports the following symptoms: " + symptoms
                + ". Suggest a triage priority and the most likely diagnosis.";

        String body = "{\"model\":\"gpt-4o\",\"messages\":[{\"role\":\"user\",\"content\":\""
                + prompt.replace("\"", "'") + "\"}]}";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Authorization", "Bearer " + openaiApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            log.error("Triage assistant call failed", e);
            return null;
        }
    }
}
