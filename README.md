# Stellarstech Project: Petstore API Tests

## Overview
This project contains automated API tests for the [Petstore Swagger API](https://petstore.swagger.io/v2). The tests validate key operations such as creating, retrieving, and deleting a user. The framework is built using **Java**, **Playwright**, and **TestNG**, and includes CI/CD integration via GitHub Actions.

## Features
- Automated tests for the following API endpoints:
    1. **POST /user**: Create a new user.
    2. **GET /user/{username}**: Retrieve details of a user.
    3. **DELETE /user/{username}**: Delete an existing user.
    4. **GET /user/{username}**: Verify the user no longer exists.
- Dynamic username generation for unique test execution.
- CI/CD pipeline configured with GitHub Actions to run tests on every push.

## Prerequisites
- **Java**: Version 18 or higher.
- **Maven**: Installed and added to the system path.
- **Git**: For cloning the repository.

## Getting Started

1. Clone the repository:
   git clone https://github.com/MariaKulgava/stellarstech-project.git
2. Navigate to the project directory:
   cd stellarstech-project
3. Run the tests:
   mvn clean test
