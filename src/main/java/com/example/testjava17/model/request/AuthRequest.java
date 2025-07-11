package com.example.testjava17.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

@Getter
@Setter
public class AuthRequest extends BaseRequest {
//    public class AuthRequest {
    private String userName;
    private String password;

    @Override
    public boolean validInput() {
        return false;
    }

    @Override
    public Stream<Object> combineField() {
        return Stream.empty();
    }
}
