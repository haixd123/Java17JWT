package com.example.testjava17.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

//@EqualsAndHashCode(callSuper = true)
@Data
public class AuthRequest extends BaseRequest {
    private String password;

    @Override
    public boolean validInput() {
        return StringUtils.hasText(password);
    }

    @Override
    public Stream<Object> combineField() {
        return Stream.of(getRequestId(), getRequestDate(), getUserName(), password);
    }
}
