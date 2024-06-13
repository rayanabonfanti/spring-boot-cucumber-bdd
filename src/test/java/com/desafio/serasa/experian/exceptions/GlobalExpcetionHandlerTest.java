package com.desafio.serasa.experian.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExpcetionHandlerTest {
    @Mock
    private AuthenticationException authenticationException;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandlerCustomException() {
        CustomException mockException = new CustomException(404, "Not Found");
        ResponseEntity<CustomException> exceptionResponseEntity = new ResponseEntity<>(
                new CustomException(404, "Not Found"),
                HttpStatus.NOT_FOUND
        );
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleErrorResponse(mockException);
        assertEquals(exceptionResponseEntity.getStatusCode(), response.getStatusCode());
        assertEquals(Objects.requireNonNull(exceptionResponseEntity.getBody()).getMessage(), Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testHandleAuthenticationException() {
        when(authenticationException.getMessage()).thenReturn("Authentication failed");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAuthenticationException(authenticationException);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, Objects.requireNonNull(response.getBody()).getErrorCode());
        assertEquals("Não autorizado, erro: Authentication failed", response.getBody().getMessage());
    }

    @Test
    void testHandleGenericException() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        Exception mockException = new Exception("Test exception");
        ResponseEntity<CustomException> expectedResponse = new ResponseEntity<>(
                new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(mockException);
        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(Objects.requireNonNull(expectedResponse.getBody()).getMessage(),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void handleValidationException_ShouldReturnErrorResponseWithBadRequestStatus() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        when(violation1.getMessage()).thenReturn("cep is required.");
        violations.add(violation1);
        when(ex.getConstraintViolations()).thenReturn(violations);
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleValidationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = responseEntity.getBody();
        Assertions.assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getErrorCode());
        assertEquals("cep is required.", errorResponse.getMessage());
    }

}
