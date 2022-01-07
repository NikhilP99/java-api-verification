package me.np99.application.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class HmacUtil {

    @Value("${app.security.secretKey}")
    private String secretKey;

    @Value("${app.security.hmacHashingAlgo}")
    private String hashingAlgo;

    public HmacUtil(){}

    public String generateHmac(String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(hashingAlgo);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), hashingAlgo);
        mac.init(secretKeySpec);

        byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }


}
