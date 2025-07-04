package com.example.testjava17.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    SUCCESS("00", "Success"),
    INVALID_INPUT("01", "Invalid Input"),
    PARTNER_NOT_FOUND("02", "Partner not found"),
    INVALID_SIGNATURE("03", "Invalid Signature"),
    UNKNOWN_ERROR("99", "Unknown Exception"),
    CUSTOM_ERROR("-99", ""),
    ;

    private final String code;
    private final String message;
}
