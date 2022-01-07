package me.np99.application.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

@Component
public class DigitalSignatureUtil {

    private final EncryptionUtil encryptionUtil;
    public static final String hashingAlgo = "SHA256withRSA";

    public DigitalSignatureUtil(EncryptionUtil encryptionUtil){
        this.encryptionUtil = encryptionUtil;
    }

    public byte[] sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature rsa = Signature.getInstance(hashingAlgo);
        rsa.initSign(encryptionUtil.getPrivateKey());
        rsa.update(message.getBytes(StandardCharsets.UTF_8));

        return rsa.sign();
    }

    public boolean verify(String message, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature rsa = Signature.getInstance(hashingAlgo);
        rsa.initVerify(encryptionUtil.getPublicKey());
        rsa.update(message.getBytes(StandardCharsets.UTF_8));

        return rsa.verify(signature);
    }


}
