# Firefly Framework - Starter Data

[![CI](https://github.com/fireflyframework/fireflyframework-starter-data/actions/workflows/ci.yml/badge.svg)](https://github.com/fireflyframework/fireflyframework-starter-data/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://openjdk.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)

> Opinionated starter for building data-processing microservices with job orchestration, data enrichers, CQRS, and event-driven capabilities for data-tier applications.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)

## Overview

Firefly Framework Starter Data is an opinionated starter that provides a production-ready architecture for building data-processing microservices. It combines job orchestration for batch and async data processing with a powerful data enrichment framework for third-party provider integration, all built on hexagonal architecture principles.

This starter is specifically designed for data-tier microservices that need to:
- Execute complex, orchestrated data processing workflows
- Integrate with external data providers (credit bureaus, financial data, business intelligence)
- Implement CQRS patterns for command/query separation
- Build event-driven architectures with automatic event publishing
- Support distributed transactions using SAGA patterns

The starter features abstract controller and service base classes that eliminate boilerplate, standardized RESTful endpoints, built-in job execution tracking, audit trails, resilience patterns (circuit breaker, retry, rate limiting), and comprehensive observability through metrics and distributed tracing.

## Features

- Job orchestration with async and sync execution modes
- Abstract data job controllers and services with standardized REST endpoints
- Data enrichment framework with pluggable `EnricherOperation` implementations
- Enrichment discovery controller for runtime operation catalog
- Smart enrichment controller with strategy-based data enrichment
- Job execution result persistence and audit trails
- CQRS auto-configuration for command/query integration
- EDA auto-configuration for event-driven data processing
- Orchestration engine integration for saga-based data flows
- Resilience patterns with circuit breaker and retry decorators
- Observability with Micrometer metrics and distributed tracing
- Enrichment caching with configurable cache key generation
- Orchestration engine support (Saga, TCC, Workflow)
- Persistence auto-configuration for job results and audit entries

## Requirements

- Java 21+
- Spring Boot 3.x
- Maven 3.9+
- PostgreSQL database (for job persistence)

## Installation

```xml
<dependency>
    <groupId>org.fireflyframework</groupId>
    <artifactId>fireflyframework-starter-data</artifactId>
    <version>26.02.06</version>
</dependency>
```

## Quick Start

```java
import org.fireflyframework.data.service.DataJobService;
import org.fireflyframework.data.controller.AbstractDataJobController;

@RestController
@RequestMapping("/api/data-jobs")
public class MyDataJobController extends AbstractDataJobController<MyRequest, MyResponse> {

    public MyDataJobController(DataJobService<MyRequest, MyResponse> service) {
        super(service);
    }
}

@Service
public class MyDataJobService extends AbstractResilientDataJobService<MyRequest, MyResponse> {

    @Override
    protected Mono<MyResponse> executeJob(MyRequest request) {
        return processData(request);
    }
}
```

## Configuration

```yaml
firefly:
  data:
    job-orchestration:
      enabled: true
      max-concurrent-jobs: 10
    enrichment:
      cache-enabled: true
      cache-ttl: 5m
    persistence:
      enabled: true
    observability:
      metrics-enabled: true
      tracing-enabled: true
```

## Documentation

Additional documentation is available in the [docs/](docs/) directory:

- [Readme](docs/README.md)

## Contributing

Contributions are welcome. Please read the [CONTRIBUTING.md](CONTRIBUTING.md) guide for details on our code of conduct, development process, and how to submit pull requests.

## License

Copyright 2024-2026 Firefly Software Solutions Inc.

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for details.
