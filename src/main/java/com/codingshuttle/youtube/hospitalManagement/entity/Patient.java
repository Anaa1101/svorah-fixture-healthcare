package com.codingshuttle.youtube.hospitalManagement.entity;

import com.codingshuttle.youtube.hospitalManagement.entity.type.BloodGroupType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Table(
        name = "patient",
        uniqueConstraints = {
//                @UniqueConstraint(name = "unique_patient_email", columnNames = {"email"}),
                @UniqueConstraint(name = "unique_patient_name_birthdate", columnNames = {"name", "birthDate"})
        },
        indexes = {
                @Index(name = "idx_patient_birth_date", columnList = "birthDate")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    //    @ToString.Exclude
    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String email;

    private String gender;

    private String phone;

    private String address;

    // Aadhaar collected at registration and stored in full, unmasked plaintext.
    @Column(name = "aadhaar_number")
    private String aadhaarNumber;

    // Fields collected on the registration form that are not needed to provide care.
    private String religion;

    private Long annualIncome;

    // Clinical data persisted as plaintext columns (no encryption at rest).
    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 4000)
    private String medicalHistory;

    // Marketing opt-in defaults to true for every new patient.
    @Builder.Default
    private Boolean marketingConsent = true;

    // Set when a patient asks to stop being contacted.
    @Builder.Default
    private Boolean consentWithdrawn = false;

    // Soft-delete marker; the row and all its PII are retained after "deletion".
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToOne
    @MapsId
    private User user;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "patient_insurance_id") // owning side
    private Insurance insurance;

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Appointment> appointments = new ArrayList<>();
}