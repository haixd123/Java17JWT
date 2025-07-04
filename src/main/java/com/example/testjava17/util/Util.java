package com.example.testjava17.util;

import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Log4j2
public class Util {
    public static final Pattern yyyyMMddHHmmss = Pattern.compile("^\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}$");
    public static final String yyyyMMddHHmmssFormat = "yyyyMMddHHmmss";

    public static String generateSignature(long userId, Stream<Object> args) {
        //TODO Lấy userId để getPrivateKey
//        var sdkPartnerAccountService = SpringContext.getBean(SdkPartnerAccountService.class);
//        var sdkPartnerAccount = sdkPartnerAccountService.getSdkPartnerAccount(userId);
//        if (sdkPartnerAccount.isEmpty()) {
//            throw new BusinessException(ErrorCode.PARTNER_NOT_FOUND);
//        }
//        var privateKey = sdkPartnerAccount.get().getPrivateKey();
//        var combineData = Stream.concat(args, Stream.of(privateKey))
//                .map(String::valueOf)
//                .collect(Collectors.joining(Constant.SEPARATE_FIELD));
//        log.info("combineData = " + combineData);
//        return Util.sha256(combineData);
        return null;
    }

    public static String sha256(String message) {
        String digest;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes(StandardCharsets.UTF_8));

            // converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (Exception ex) {
            digest = "";
        }
        return digest;
    }
}
