package com.eighttoten.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
