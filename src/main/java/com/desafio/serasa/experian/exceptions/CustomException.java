package com.desafio.serasa.experian.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends Exception {
    private final int errorCode;
    private final String message;
}
