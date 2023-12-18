package com.svalero.vuelosapi.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorResponse {
    private int code;
    private String message;
    private Map<String, String> errors;

    private ErrorResponse(int errorCode, String errorMessage) {
        code = errorCode;
        message = errorMessage;
        errors = new HashMap<>();
    }

    public static ErrorResponse generalError(int code, String message) {
        return new ErrorResponse(code, message);
    }

}
