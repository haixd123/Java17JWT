package com.example.testjava17.model.request;

import lombok.Data;

import java.util.stream.Stream;

@Data
public class UserRequest extends BaseRequest {
    private long id;
    private String name;
    private String password;

    @Override
    public boolean validInput() {
        return false;
    }

    @Override
    public Stream<Object> combineField() {
        return Stream.of(getUserName());
    }
}
