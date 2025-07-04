package com.example.testjava17.model.request;

import lombok.Data;

@Data
public class UserRequest {
    private long id;
    private String name;
    private String userName;
    private String password;
}
