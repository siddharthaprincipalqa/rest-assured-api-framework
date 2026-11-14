# REST Assured API Automation Framework

A Java API testing portfolio project using GoREST to practise functional testing, validation, authentication, and automated execution.

## Technology Stack

- Java 21
- REST Assured
- TestNG
- Maven
- Jackson
- NetworkNT JSON Schema Validator
- GitHub Actions

## Test Coverage

- Retrieve users by ID
- Filter users and verify pagination
- Create users and verify saved details
- Update status using PATCH
- Delete users and verify deletion
- Validate blank names and invalid email formats
- Reject duplicate emails
- Verify multiple validation errors
- Verify missing and invalid bearer tokens
- Validate response structure using JSON Schema
- Exercise name-length boundaries

## Project Structure

```text
src/test/java/org/example/
├── clients/   # API requests and responses
├── config/    # Shared configuration and authentication
├── data/      # Request bodies and unique test data
├── helpers/   # Reusable verification and cleanup
└── tests/     # Test scenarios and assertions

src/test/resources/
├── config.properties
└── schemas/

.github/workflows/api-tests.yml
testng.xml
testng-smoke.xml
```

## Prerequisites

- JDK 21
- Maven available on PATH
- A valid GoREST access token

## Setup

Clone the repository:

```bash
git clone https://github.com/siddharthaprincipalqa/rest-assured-api-framework.git
cd rest-assured-api-framework
```

Set the environment variable `GOREST_TOKEN` to your own GoREST token.

Do not commit tokens or store them in source code.

The default API address is configured in:

```text
src/test/resources/config.properties
```

## Run Tests

Run regression tests:

```bash
mvn test
```

Run smoke tests:

```bash
mvn test -DsuiteXmlFile=testng-smoke.xml
```

In PowerShell, quote the property argument if needed:

```powershell
mvn test "-DsuiteXmlFile=testng-smoke.xml"
```

Override the base URL:

```bash
mvn test -DbaseUrl=https://gorest.co.in
```

## Reports

Maven generates test results under:

```text
target/surefire-reports/
```

## GitHub Actions

The workflow currently runs manually:

1. Open the repository's Actions tab.
2. Select API Tests.
3. Select Run workflow.

CI reads `GOREST_TOKEN` from a GitHub repository secret.

The workflow file also defines report upload as an artifact named
`api-test-reports`, retained for seven days.

## Test Design

- Tests define expected behaviour separately from API request methods.
- Each lifecycle invocation creates its own user.
- UUID-based emails reduce duplicate-data interference.
- GET requests verify persisted changes.
- Cleanup runs in finally blocks after creation and ID extraction.
- Validation scenarios isolate individual rules and cover combined errors.
- Schema checks complement field-value assertions.

## Limitations

- This project depends on an external public practice API.
- Public seed records may change, affecting tests with fixed IDs.
- Cleanup requires a usable ID from the creation response.
- A cleanup failure can currently mask an earlier assertion failure.
- Parallel execution has not yet been verified.
- This is functional API automation, not a load-testing or comprehensive
  security-testing suite.

## Project Status

Actively developed as a learning and QA portfolio project.