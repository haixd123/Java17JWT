package com.example.testjava17.validatior;

import com.example.testjava17.Exception.BusinessException;
import com.example.testjava17.Exception.JwtValidationException;
import com.example.testjava17.model.request.BaseRequest;
import com.example.testjava17.util.*;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class BaseRequestValidator implements ConstraintValidator<ValidBaseRequest, BaseRequest> {
    @Value("${secureCode.secret.key}")
    private String secureCodeSecretKey;

    @Autowired
    private AesUtil aesUtil;

    @Override
    public boolean isValid(BaseRequest baseRequest, ConstraintValidatorContext context) {
        var baseValidInput = baseRequest.baseValidInput();
        var validInput = baseRequest.validInput();
        var secureCode = "";
        if (!baseValidInput || !validInput) {
            log.info("baseValidInput is : {}, validInput is : {}", baseValidInput, validInput);
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        try {
            var authToken = aesUtil.decrypt(baseRequest.getAuthToken());
            secureCode = authToken.split("#")[1];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            // validate secureCode
            var combineData = Stream.concat(baseRequest.combineField(), Stream.of(secureCodeSecretKey))
                    .map(String::valueOf)
                    .collect(Collectors.joining("|"));
            log.info("combineData = " + combineData);
            String signature = Util.sha256(combineData);
            if (!secureCode.equals(signature)) {
                log.info("invalid signature , secureCode = {}, signature = {}", baseRequest.getSecureCode(), signature);
                throw new BusinessException(ErrorCode.INVALID_SIGNATURE);
            }
            return true;
        } catch (BusinessException ex) {
            throw new JwtValidationException(ex.getMessage());
        }
    }
}
