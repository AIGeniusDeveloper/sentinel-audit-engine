# Sentinel Audit Engine (S.A.C.)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![LangChain4j](https://img.shields.io/badge/LangChain4j-0.29.0-blue.svg)](https://github.com/langchain4j/langchain4j)

A production-ready **Multi-Agent Compliance Audit System** built with Spring Boot and LangChain4j, demonstrating enterprise-grade architecture patterns and AI-driven decision making.

## 🎯 Overview

Sentinel Audit Engine validates complex financial transactions against regulatory requirements using autonomous AI agents. The system combines RAG (Retrieval-Augmented Generation) with external service integration to provide real-time compliance reports.

## 🏗️ Architecture

### Multi-Agent System (LangGraph)

The system uses **LangGraph4j** to orchestrate a state-driven workflow with three specialized agents:

```
kyc_check → rag_search → generate_report
```

- **KycService** (Node): Verifies customer identity and transaction limits via external providers
- **RagService** (Node): Searches regulatory documents using pgvector embeddings
- **AuditAgent** (AI Node): Synthesizes findings into structured compliance reports using LLM

**State Management**: `AuditState` tracks transaction context, KYC results, regulations, and final report across nodes.

### Design Principles
- ✅ **SOLID**: Interface-based design with Dependency Inversion
- ✅ **DDD**: Multi-module architecture with clear bounded contexts
- ✅ **Hexagonal Architecture**: Ports & Adapters pattern (Application/Infrastructure)
- ✅ **State Machine**: LangGraph4j for explicit workflow orchestration
- ✅ **Enterprise Patterns**: Exception handling, validation, comprehensive testing

## 🚀 Quick Start

### Prerequisites
```bash
# Required
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- Ollama (for local LLM)
```

### Installation

1. **Clone & Navigate**
```bash
cd /Users/boubah/Downloads/longchainStudio/sentinel-audit
```

2. **Start Infrastructure**
```bash
docker-compose up -d
```

3. **Start Ollama & Pull Models**
```bash
ollama serve
ollama pull llama3
ollama pull nomic-embed-text
```

4. **Run Application**
```bash
cd sentinel-api
mvn spring-boot:run
```

## 📡 API Usage

### Audit Transaction
```bash
curl -X POST http://localhost:8080/api/audit \
-H "Content-Type: application/json" \
-d '{
  "transactionId": "TXN-12345",
  "customerId": "HIGH_RISK_USER_001",
  "amount": 15000.00,
  "currency": "EUR",
  "type": "WIRE_TRANSFER",
  "destinationCountry": "Cayman Islands"
}'
```

### Response Example
```json
{
  "verdict": "NON-COMPLIANT",
  "kycStatus": "FLAGGED (PEP_MATCH)",
  "regulations": ["Article 12: EDD required for >10K EUR"],
  "recommendation": "REJECT"
}
```

### Validation Errors
```bash
# Missing required field
curl -X POST http://localhost:8080/api/audit \
-H "Content-Type: application/json" \
-d '{"amount": -100}'

# Response:
{
  "timestamp": "2025-12-02T18:40:00",
  "status": 400,
  "error": "Validation Failed",
  "validationErrors": [
    {"field": "amount", "message": "Amount must be positive"},
    {"field": "customerId", "message": "Customer ID is required"}
  ]
}
```

## 🧪 Testing

```bash
# Run all tests from root
mvn test
```

**Test Coverage:**
- **sentinel-infrastructure**: `RagServiceTest`, `KycServiceTest` (Unit/Integration)
- **sentinel-api**: `AuditControllerTest` (Web Slice)

## 📦 Deployment

### Docker
```bash
docker build -t sentinel-audit:latest .
docker run -p 8080:8080 sentinel-audit:latest
```

### Kubernetes
```bash
kubectl apply -f k8s-deployment.yml
```

## 🛠️ Technology Stack

| Category | Technology |
|----------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.2.3 |
| AI/LLM | LangChain4j 0.29.0, LangGraph4j 1.7.3, Ollama |
| Database | PostgreSQL 16 + pgvector |
| Validation | Jakarta Bean Validation |
| Testing | JUnit 5, Mockito, AssertJ |
| Build | Maven (Multi-Module) |
| Containerization | Docker, Kubernetes |

## 📚 Project Structure

```
sentinel-audit/
├── pom.xml                     # Parent POM
├── sentinel-domain/            # Core Domain (Entities, Repositories Interfaces)
│   └── src/main/java/com/sentinel/audit/domain/
├── sentinel-application/       # Use Cases & DTOs
│   └── src/main/java/com/sentinel/audit/application/
├── sentinel-infrastructure/    # Adapters (JPA, AI Agents) & Config
│   └── src/main/java/com/sentinel/audit/infrastructure/
├── sentinel-api/               # REST API & Main Application
│   └── src/main/java/com/sentinel/audit/api/
├── docker-compose.yml          # Local Development
└── k8s-deployment.yml          # Kubernetes Manifests
```

## 🔒 Security & Compliance

- **Input Validation**: Bean Validation on all DTOs
- **Error Handling**: Global exception handler with structured responses
- **Audit Trail**: All transactions logged to database
- **Type Safety**: Enums for critical business values

## 🎓 Learning Outcomes

This project demonstrates:
- **State-driven AI workflows** with LangGraph4j
- **Multi-agent orchestration** (KYC, RAG, Audit synthesis)
- **RAG implementation** with pgvector embeddings
- **DDD multi-module architecture** (Domain/Application/Infrastructure/API)
- **Hexagonal Architecture** with Ports & Adapters
- **SOLID principles** and Clean Code practices
- **Comprehensive testing** strategies (Unit, Integration, Web Slice)

## 📄 License

MIT License - See LICENSE file for details

## 👥 Contributing

Contributions welcome! Please read CONTRIBUTING.md first.

---

**Built with ❤️ for demonstrating senior-level Java development practices**
