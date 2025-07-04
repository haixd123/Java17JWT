package com.example.testjava17.model.request;

import com.example.testjava17.util.Util;
import com.example.testjava17.validatior.ValidBaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

@Getter
@Setter
@ValidBaseRequest
public abstract class BaseRequest {
    private String requestId;
    private String requestDate;
    private String userName;
    private String secureCode;

    public abstract boolean validInput();

    public abstract Stream<Object> combineField();

    public final boolean baseValidInput() {
        return StringUtils.hasLength(requestId)
                && StringUtils.hasLength(requestDate) && Util.yyyyMMddHHmmss.matcher(requestDate).matches()
                && StringUtils.hasLength(userName)
                && StringUtils.hasLength(secureCode);
    }
}
