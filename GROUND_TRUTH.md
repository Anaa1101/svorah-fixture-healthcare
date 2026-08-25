# GROUND TRUTH — svorah-fixture-healthcare

> Answer key for this fixture. Every row is a DPDP violation **deliberately planted** into a real
> open-source Spring Boot hospital-management app, to test SVORAH's detection.
> Score a scan as: **recall** = planted violations detected / 19 · **precision** = true findings / all findings.
>
> Catalog IDs reference `DPDP_VIOLATION_CATALOG.md`. Phase: all are **PRESENCE** (bad pattern in code).

**Base repo:** `Anuj-Kumar-Sharma/spring-boot-data-jpa-hospital-management-system` (forked, then seeded).
**Stack:** Java 21 · Spring Boot 3.5 · JPA · PostgreSQL.
**Planted count:** 19 PRESENCE violations.

| # | Catalog | DPDP | Sev | File | Where | What was planted |
|---|---------|------|-----|------|-------|------------------|
| 1 | A1 | S.6 | High | `security/AuthService.java` | `signUpInternal()` | Patient + all PII persisted with no consent requested or recorded before the write |
| 2 | A3 | S.6(1) | High | `entity/Patient.java:69` | `marketingConsent` | Marketing opt-in defaults to `true` for every new patient |
| 3 | A6 | S.6(4) | Critical | `service/NotificationService.java` | `sendAppointmentReminder()` | `consentWithdrawn` flag never checked — opted-out patients still texted |
| 4 | C1 | S.4/6 | High | `service/CampaignService.java` | `sendHealthCampPromo()` | Emails collected for reminders reused for marketing to all patients, no consent check |
| 5 | D1 | S.4(2) | High | `dto/SignUpRequestDto.java` + `AuthService` | signup fields | Registration collects Aadhaar, religion, annual income — none needed for care |
| 6 | D3 | S.4(2) | High | `entity/Patient.java:53` | `aadhaarNumber` | Full Aadhaar stored, unmasked, no tokenisation |
| 7 | E1 | S.8(5) | High | `entity/Patient.java:62` | `diagnosis`, `medicalHistory` | Clinical data stored as plaintext columns, no encryption at rest |
| 8 | E2 | S.8(5) | Critical | `service/StaffAccountService.java` | `md5()` | Staff passwords hashed with unsalted MD5 |
| 9 | E3 | S.8(5) | Critical | `entity/Patient.java:53` | `aadhaarNumber` | Aadhaar (sensitive ID) stored plaintext & unmasked |
| 10 | E4 | S.8(5) | High | `service/PatientService.java` | `getPatientEntityById()`, `searchByAadhaarAndPhone()` | Full patient record + Aadhaar/phone written to application logs |
| 11 | E5 | S.8(5) | High | `controller/PatientController.java` | `GET /patients/search` | Aadhaar & phone accepted as URL query params |
| 12 | E7 | S.8(5) | Critical | `resources/application.properties:6,9-12` | config | Hardcoded DB password, OpenAI key, AWS keys, SMS key in committed config |
| 13 | F1 | S.8(5) | Critical | `service/TriageAssistantService.java` | `suggestTriage()` | Patient name, Aadhaar, DOB, history sent raw to OpenAI, no redaction. Recipient=OpenAI → **suspected** cross-border; §16 verdict deferred |
| 14 | F3 | S.8(5) | High | `service/FileStorageService.java` | `AWS_REGION="us-east-1"` | Patient scans → AWS S3 (recipient named); literal region hint `us-east-1` → **suspected** cross-border; §16 verdict deferred to cloud scan / DPO |
| 15 | F4 | S.8 | High | `controller/AdminController.java` | `GET /admin/export/patients` | Bulk CSV dump of every patient, all PII fields, no masking |
| 16 | G3 | S.8(7) | Medium | `service/PatientService.java` | `deletePatient()` | Soft-delete only (`isDeleted=true`) — PII retained indefinitely |
| 17 | K1 | S.8(5) | High | `resources/data.sql` | seed inserts | Real-looking patient PII (Aadhaar, phone, diagnosis) committed to VCS |
| 18 | K3 | S.8(5) | Critical | `controller/DebugController.java` + `security/WebSecurityConfig.java` | `/debug/patient/{id}` | Unauthenticated endpoint returns full patient record (`/debug/**` is permitAll) |
| 19 | K4 | S.8(5) | Critical | `controller/PatientController.java` | `GET /patients/{id}` | No ownership check — any authenticated user reads any patient (IDOR) |

## Notes
- Violation #12 (E7) includes the repo's **pre-existing** hardcoded `jwt.secretKey`, plus DB/OpenAI/AWS/SMS
  secrets added during seeding.
- All secrets/keys here are **fake** placeholders for scanner testing — not live credentials.
- **Cross-border (§16):** the code agent may only mark F1/F3 as **suspected** (recipient named,
  plus a literal region hint for F3). It must not *confirm* residency — that verdict belongs to
  the cloud scan, a `.svorah.yml` `data_residency` declaration, or the DPO. Same rule as the
  taint corpus (`svorah-test-repo`).
- ABSENCE-class violations (no consent record, no erasure endpoint, no retention TTL, no audit log,
  no privacy notice) are intentionally **not** planted in this phase; see the catalog's phase-2 list.
