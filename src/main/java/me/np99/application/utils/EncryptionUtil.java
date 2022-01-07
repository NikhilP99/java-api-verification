package me.np99.application.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
@Getter
public class EncryptionUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public static final String privateKeyPath = "private_key";
    public static final String publicKeyPath = "public_key";

    public EncryptionUtil() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
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

}
