package com.example.testjava17.model.response;

import com.example.testjava17.util.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class BaseResponse {
    private String errorCode;
    private String message;
    private Object data;
    private Object optional;

    public static BaseResponse success(ErrorCode errorCode, Object data) {
        return responseSuccess(errorCode, data, null);
    }

    public static BaseResponse success(ErrorCode errorCode, Object data, Object optional) {
        return responseSuccess(errorCode, data, optional);
    }

    public static BaseResponse successCustom(String errorCode, String message, Object data, Object optional) {
        return responseSuccessCustom(errorCode, message, data, optional);
    }

    public static BaseResponse responseSuccess(ErrorCode errorCode, Object data, Object optional) {
        return BaseResponse.builder()
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(data)
                .optional(optional)
                .build();
    }

    public static BaseResponse responseSuccessCustom(String errorCode, String message, Object data, Object optional) {
        return BaseResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .data(data)
                .optional(optional)
                .build();
    }

    public static BaseResponse error(ErrorCode errorCode) {
        return responseError(errorCode);
    }

    public static BaseResponse errorCustom(String errorCode, String message) {
        return responseErrorCustom(errorCode, message);
    }


    public static BaseResponse responseError(ErrorCode errorCode) {
        return BaseResponse.builder()
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static BaseResponse responseErrorCustom(String errorCode, String message) {
        return BaseResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}
