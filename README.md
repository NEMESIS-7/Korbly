# Korbly – Institutional Private Credit Platform

Institutional-grade private credit platform for Africa, built to connect SMEs, investors, regulators, and insurers in a transparent and compliant way.

Think of it as the operating system for private credit markets — from SME onboarding → credit evaluation → syndication → regulatory oversight → valuation.

##  Vision

Korbly is not a retail fintech. It is designed for institutional investors, regulators, and enterprises — focusing first on Ghana and then scaling across Africa.

The platform de-risks lending by enforcing compliance, transparency, and discipline at every stage:

- **SMEs** undergo structured onboarding and KYC/KYB
- **Creditworthiness** is evaluated using institutional metrics (DSCR, Altman Z-Score, Ohlson O-Score, leverage ratios)
- **Deals** are syndicated across investors via tranches and allocations
- **Regulators** and auditors have real-time oversight into deals, allocations, and audit logs
- **Valuation** tools provide IRR/NPV analysis to investors and analysts

## Architecture

### Core Modules

**SME Module**  
Draft onboarding, document uploads, RAG (Red/Amber/Green) readiness, submission, credit evaluation trigger, KYC approval.

**Credit Module**  
Risk scoring (Altman, Ohlson, DSCR, ICR, Leverage, LTV), stress testing (FX/cyclical), credit memo generation.

**Investor Module**  
Institutional onboarding, mandate definition (sectors, tenors, currencies), allocation to eligible deals.

**Syndication Module**  
Tranches, allocations, fill-percent logic, oversubscription handling, state transitions (Draft → Public → Closed).

**Regulator Module**  
Read-only dashboards, regulator onboarding by Admin, audit log queries, deal & tranche views.

**Term Sheet & CPs (Conditions Precedent)**  
Draft, version, execute term sheets. Checklist of CPs (e.g., KYC cleared, collateral registered, insurance bound) required before funding.

**Valuation Module**  
IRR/NPV calculators, repayment schedules, sensitivity analysis.

### Cross-Cutting Concerns

- **RBAC** – enforced at service & controller level (ADMIN, INVESTOR, SME, REGULATOR)
- **KYC/KYB** – mandatory gating before syndication or allocations
- **Audit Trail** – every action logs actorId, action, entityType, entityId, timestamp
- **File Storage** – investor/SME docs stored in S3 with presigned download URLs
- **Security** – JWT auth, OTP verification, secure cookies, strict SameSite policies

## API Highlights

### SMEs
- `POST /smes/draft` – create draft SME
- `PATCH /smes/{id}` – update SME profile
- `POST /smes/{id}/documents` – upload docs
- `GET /smes/{id}/rag` – readiness (Red/Amber/Green)
- `POST /smes/{id}/submit` – submit for credit eval

### Credit
- `POST /credit/evaluate/{smeId}` – generate credit memo
- `GET /credit/get-memo/{memoId}` – fetch credit memo

### Investors
- `POST /investors` – onboard investor
- `PUT /investors/{id}/mandate` – set mandate
- `GET /investors/{id}/deals` – view eligible deals

### Syndication
- `POST /allocations` – allocate to tranche
- `GET /deals` – list all deals/tranches

### Regulators
- `POST /regulator/create` – admin creates regulator
- `GET /regulator/deals` – view deals overview
- `GET /regulator/audit-logs` – view system logs

## Tech Stack

- **Backend:** Java 21, Spring Boot 3, Maven
- **DB:** PostgreSQL + Flyway migrations
- **Cache:** Redis
- **Storage:** AWS S3 (doc storage, presigned URLs)
- **Deployment:** Heroku / DigitalOcean
- **Auth:** JWT, OTP, role-based security

## ⚡ Getting Started

### Clone Repo
```bash
    git clone https://github.com/korbly/korbly-backend.git
    cd korbly-backend
```

### Configure Env
```bash
    DB_URL=jdbc:postgresql://localhost:5432/korbly
    DB_USER=korbly
    DB_PASS=secret
    BUCKET_NAME=korbly-files
    JWT_SECRET=supersecret
```

### Run Migrations
```bash
   mvn flyway:migrate
```

### Start App
```bash
   mvn spring-boot:run
```

### Access APIs
```
http://localhost:8080/api/v1/
```

## Governance & Compliance

- **Conditions Precedent (CPs)** block deals from advancing to "Ready to Fund" unless approved
- **Audit Logging** ensures every material action is immutable
- **Regulator Dashboards** provide oversight without altering data

## Roadmap (Phase-1 → MVP)

- [x] SME onboarding (draft, RAG, submit)
- [x] Credit memos (Altman/Ohlson + ratios)
- [x] Investor onboarding & mandates
- [x] Syndication allocations & deal states
- [x] Regulator dashboards & audit logs
- [x] Term sheets & CPs (draft/execution)
- [x] Valuation engine (IRR/NPV/schedules)
- [x] API documentation (OpenAPI/Swagger)

## Contributing

1. Fork repo, create feature branch
2. Write clean code, add tests
3. Submit PR with clear description

##  License

MIT License – free to use, modify, and distribute.