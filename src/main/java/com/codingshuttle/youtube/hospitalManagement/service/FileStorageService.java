package com.codingshuttle.youtube.hospitalManagement.service;

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
public class FileStorageService {

    // Patient scans and lab reports are uploaded to an S3 bucket hosted in the
    // us-east-1 (N. Virginia) region — personal and health data transferred out of
    // India with no assessment of the destination country.
    private static final String AWS_REGION = "us-east-1";
    private static final String BUCKET = "hospital-patient-scans";

    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretAccessKey}")
    private String awsSecretAccessKey;

    public void uploadPatientScan(Long patientId, String fileName, byte[] content) {
        String url = "https://" + BUCKET + ".s3." + AWS_REGION + ".amazonaws.com/patients/"
                + patientId + "/" + fileName;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("x-amz-access-key", awsAccessKeyId)
                    .PUT(HttpRequest.BodyPublishers.ofByteArray(content))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Uploaded patient scan to {}", url);
        } catch (Exception e) {
            log.error("Scan upload failed", e);
        }
    }
}
