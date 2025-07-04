package com.example.testjava17.util;

public interface Constant {
    interface CONTENT_TYPE {
        String APPLICATION_JSON_TYPE = "application/json;charset=UTF-8";

    }

    interface SPRING_SCAN_ENTITY {
        String FYNA_PACKAGE_ENTITIES = "com.example.testjava17.model.entity.fyna";
        String WALLET_PACKAGE_ENTITIES = "com.example.testjava17.model.entity.wallet";
    }

    interface SPRING_SCAN_REPOSITORY {
        String FYNA_PACKAGE_REPOSITORY = "com.example.testjava17.repository.fyna";
        String WALLET_PACKAGE_REPOSITORY = "com.example.testjava17.repository.wallet";
    }

}
