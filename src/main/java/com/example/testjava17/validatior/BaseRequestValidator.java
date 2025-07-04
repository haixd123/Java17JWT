package com.example.testjava17.validatior;

import com.example.testjava17.Exception.BusinessException;
import com.example.testjava17.model.entity.fyna.UsersEntity;
import com.example.testjava17.model.request.BaseRequest;
import com.example.testjava17.repository.fyna.UsersRepository;
import com.example.testjava17.util.ErrorCode;
import com.example.testjava17.util.Util;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class BaseRequestValidator implements ConstraintValidator<ValidBaseRequest, BaseRequest> {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public boolean isValid(BaseRequest baseRequest, ConstraintValidatorContext context) {
        var baseValidInput = baseRequest.baseValidInput();
        var validInput = baseRequest.validInput();
        if (!baseValidInput || !validInput) {
            log.info("baseValidInput is : {}, validInput is : {}", baseValidInput, validInput);
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        // validate secureCode
        UsersEntity users = usersRepository.findByUserName(baseRequest.getUserName());
        var privateKey = users.getPrivateKey() != null ? users.getPrivateKey() : "";
        var combineData = Stream.concat(baseRequest.combineField(), Stream.of(privateKey))
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        log.info("combineData = " + combineData);
        String signature = Util.sha256(combineData);
        if (!baseRequest.getSecureCode().equals(signature)) {
            log.info("invalid signature , secureCode = {}, signature = {}", baseRequest.getSecureCode(), signature);
            throw new BusinessException(ErrorCode.INVALID_SIGNATURE);
        }
        return true;
    }
}
