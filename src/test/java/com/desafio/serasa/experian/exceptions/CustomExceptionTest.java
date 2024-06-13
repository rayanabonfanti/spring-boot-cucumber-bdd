package com.desafio.serasa.experian.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExceptionTest {

    @Test
    void testCustomException() {
        int errorCode = 404;
        String errorMessage = "Resource not found";

        CustomException customException = new CustomException(errorCode, errorMessage);

        assertEquals(errorCode, customException.getErrorCode());
        assertEquals(errorMessage, customException.getMessage());
    }

    @Test
    void testCustomExceptionWithSuperConstructor() {
        int errorCode = 500;
        String errorMessage = "Internal server error";
        String additionalMessage = "Additional information";

        CustomException customException = new CustomException(errorCode, errorMessage + " - " + additionalMessage);

        assertEquals(errorCode, customException.getErrorCode());
        assertEquals(errorMessage + " - " + additionalMessage, customException.getMessage());
    }
}
