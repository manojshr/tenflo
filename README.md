# 🛰️ Tenflo — Multi-Tenant Deployment & Usage Monitoring Platform

**Tenflo** is a secure, event-driven platform designed to track GitHub deployment activity and tenant-specific API usage, with automated alerting and secret isolation — all in a multi-tenant environment.

---

## 🚀 Features

- 🔐 **Per-Tenant Security**  
  Uses AWS IAM, STS, and Secrets Manager to isolate credentials and secrets per tenant.

- 📩 **Webhook Intake with HMAC Validation**  
  Verifies incoming GitHub deployment webhooks using tenant-specific secrets.

- 📊 **Usage Tracking**  
  Periodically polls mocked usage APIs using secure, per-tenant credentials.

- 🧵 **Event-Driven Architecture**  
  Kafka is used as the backbone for both deployment and usage data pipelines.

- ⚙️ **Infrastructure as Code**  
  Terraform automates tenant onboarding with isolated IAM roles, secrets, and policies.

---

## 🔁 High-Level Flow

1. **Deployment Event Intake**  
   GitHub sends a webhook → verified by HMAC → published to Kafka (`deployments` topic)

2. **Usage Polling**  
   Scheduled service assumes tenant IAM role → fetches secret/API key → polls usage API → publishes to Kafka (`usage` topic)

3. **Analysis & Alerting**  
   Kafka consumers analyze deployment and usage events → alert if invalid refs or budget breaches occur

---

## 📦 Tech Stack

- **Spring Boot** (Java 21)
- **Apache Kafka**
- **AWS IAM, STS, Secrets Manager**
- **Terraform** (Infra Provisioning)
- **LocalStack** (for local AWS simulation)
- **GitHub Webhooks**
