# Firefly Framework - Data

[![CI](https://github.com/fireflyframework/fireflyframework-data/actions/workflows/ci.yml/badge.svg)](https://github.com/fireflyframework/fireflyframework-data/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://openjdk.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)

> Data processing library with job orchestration, data enrichment, CQRS integration, and observability for core-data microservices.

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

Firefly Framework Data provides a comprehensive data processing architecture for building core-data microservices. It includes job orchestration for batch and async data processing, a data enrichment framework with pluggable enricher operations, and integration with the framework's CQRS, EDA, and transactional engine modules.

The library features abstract controller and service base classes that standardize RESTful endpoints for data jobs and enrichment operations. It includes built-in support for job execution tracking, audit trails, resilience patterns, and observability through metrics and tracing.

The data enrichment subsystem allows defining enricher operations as annotated beans that are automatically discovered, registered, and exposed through a unified API with caching, validation, and event publishing capabilities.

## Features

- Job orchestration with async and sync execution modes
- Abstract data job controllers and services with standardized REST endpoints
- Data enrichment framework with pluggable `EnricherOperation` implementations
- Enrichment discovery controller for runtime operation catalog
- Smart enrichment controller with strategy-based data enrichment
- Job execution result persistence and audit trails
- CQRS auto-configuration for command/query integration
- EDA auto-configuration for event-driven data processing
- Transactional engine integration for saga-based data flows
- Resilience patterns with circuit breaker and retry decorators
- Observability with Micrometer metrics and distributed tracing
- Enrichment caching with configurable cache key generation
- Step event bridge for transactional engine coordination
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
    <artifactId>fireflyframework-data</artifactId>
    <version>26.01.01</version>
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
