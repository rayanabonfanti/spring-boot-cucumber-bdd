Feature: Cadastro de Pessoa

  Scenario: Cadastro de pessoa com sucesso
    Given the user has a valid JWT token
    When the user sends a valid pessoa data
    Then the user should receive a success response
