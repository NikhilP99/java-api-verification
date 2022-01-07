package me.np99.application.utils;

import lombok.Getter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
@Getter
public class EncryptionUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final Cipher cipher;

    public static final String privateKeyPath = "private_key";
    public static final String publicKeyPath = "public_key";

    public EncryptionUtil() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance("RSA");
        this.privateKey = getPrivate();
        this.publicKey = getPublic();
    }

    public PrivateKey getPrivate() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(new File(privateKeyPath).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePrivate(spec);
    }

    public PublicKey getPublic() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(new File(publicKeyPath).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePublic(spec);
    }

    public String encrypt(String message) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(this.cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String message) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        this.cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(this.cipher.doFinal(Base64.decodeBase64(message)), StandardCharsets.UTF_8);
    }

}
