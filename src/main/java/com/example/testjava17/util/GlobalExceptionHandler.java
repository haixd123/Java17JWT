package com.example.testjava17.util;

import com.example.testjava17.Exception.BusinessException;
import com.example.testjava17.Exception.JwtValidationException;
import com.example.testjava17.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<?> handleJwtError(JwtValidationException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException ex) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setErrorCode(ex.getErrorCode().getCode());
        baseResponse.setMessage(ex.getErrorCode().getMessage());
        return baseResponse;
    }

}
