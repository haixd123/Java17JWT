package com.example.testjava17.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    SUCCESS("00", "Success"),
    INVALID_INPUT("01", "Invalid Input"),
    WRONG_INPUT("02", "Wrong user/password"),
    PARTNER_NOT_FOUND("03", "Partner not found"),
    INVALID_SIGNATURE("04", "Invalid Signature"),
    INVALID_SESSION("401", "Invalid session"),
    INVALID_REFRESH_TOKEN("402", "Invalid refresh token"),
    UNKNOWN_ERROR("99", "Unknown Exception"),
    CUSTOM_ERROR("-99", ""),
    ;

    private final String code;
    private final String message;
}
