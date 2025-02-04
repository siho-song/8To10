package com.eighttoten.support;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse of(int code, String message){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.code = code;
        errorResponse.message = message;
        return errorResponse;
    }
}
