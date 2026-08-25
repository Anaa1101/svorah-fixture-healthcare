-- Seed data committed with real-looking patient PII: full Aadhaar numbers, phone
-- numbers and clinical diagnoses in plaintext, straight into version control.
INSERT INTO patient (name, gender, birth_date, email, blood_group, phone, aadhaar_number, diagnosis, marketing_consent)
VALUES
    ('Aarav Sharma', 'MALE', '1990-05-10', 'aarav.sharma@gmail.com', 'O_POSITIVE', '+91-98200-11234', '4321 8765 2109', 'Type 2 diabetes mellitus; on metformin', true),
    ('Diya Patel', 'FEMALE', '1995-08-20', 'diya.patel@gmail.com', 'A_POSITIVE', '+91-99300-55678', '5678 1234 9087', 'Major depressive disorder; sertraline 50mg', true),
    ('Dishant Verma', 'MALE', '1988-03-15', 'dishant.verma@gmail.com', 'A_POSITIVE', '+91-98765-43210', '9012 3456 7823', 'HIV positive; antiretroviral therapy', true),
    ('Neha Iyer', 'FEMALE', '1992-12-01', 'neha.iyer@gmail.com', 'AB_POSITIVE', '+91-90040-22119', '2345 6789 0145', 'Pregnancy, first trimester', true),
    ('Kabir Singh', 'MALE', '1993-07-11', 'kabir.singh@gmail.com', 'O_POSITIVE', '+91-70420-98800', '6789 0123 4567', 'Hypertension; amlodipine 5mg', true);

INSERT INTO doctor (name, specialization, email)
VALUES
    ('Dr. Rakesh Mehta', 'Cardiology', 'rakesh.mehta@example.com'),
    ('Dr. Sneha Kapoor', 'Dermatology', 'sneha.kapoor@example.com'),
    ('Dr. Arjun Nair', 'Orthopedics', 'arjun.nair@example.com');

INSERT INTO appointment (appointment_time, reason, doctor_id, patient_id)
VALUES
  ('2025-07-01 10:30:00', 'General Checkup', 1, 2),
  ('2025-07-02 11:00:00', 'Skin Rash', 2, 2),
  ('2025-07-03 09:45:00', 'Knee Pain', 3, 3),
  ('2025-07-04 14:00:00', 'Follow-up Visit', 1, 1),
  ('2025-07-05 16:15:00', 'Consultation', 1, 4),
  ('2025-07-06 08:30:00', 'Allergy Treatment', 2, 5);