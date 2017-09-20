package com.euihwan.study.commons;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private String message;
    private String code;
    private List<FieldError> errors;

    //TODO
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }
}
