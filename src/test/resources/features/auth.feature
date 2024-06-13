Feature: Authentication

  Scenario: Successful login
    Given the user provides valid credentials
    When the user tries to login
    Then the user should receive a valid JWT token
