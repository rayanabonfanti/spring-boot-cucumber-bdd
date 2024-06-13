package com.desafio.serasa.experian.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void testErrorResponse() {
        int errorCode = 404;
        String errorMessage = "Resource not found";

        ErrorResponse customException = new ErrorResponse(errorCode, errorMessage);

        assertEquals(errorCode, customException.getErrorCode());
        assertEquals(errorMessage, customException.getMessage());
    }

    @Test
    void testErrorResponseWithSuperConstructor() {
        int errorCode = 500;
        String errorMessage = "Internal server error";
        String additionalMessage = "Additional information";

        ErrorResponse customException = new ErrorResponse(errorCode, errorMessage + " - " + additionalMessage);

        assertEquals(errorCode, customException.getErrorCode());
        assertEquals(errorMessage + " - " + additionalMessage, customException.getMessage());
    }
}
