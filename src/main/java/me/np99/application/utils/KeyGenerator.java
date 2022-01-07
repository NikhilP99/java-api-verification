package me.np99.application.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

@Slf4j
@Getter
public class KeyGenerator {

    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public static final String privateKeyPath = "private_key";
    public static final String publicKeyPath = "public_key";

    public KeyGenerator(int keyLength) throws NoSuchAlgorithmException {
        this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyPairGenerator.initialize(keyLength);
    }

    private void getKeys(){
        this.keyPair = this.keyPairGenerator.generateKeyPair();
        this.privateKey = this.keyPair.getPrivate();
        this.publicKey = this.keyPair.getPublic();
    }

    public void saveToFile(String path, byte[] key) throws IOException {
        File file = new File(path);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public static void main(String[] args){
        KeyGenerator keyGenerator;

        try {
            keyGenerator = new KeyGenerator(1024);
            log.info("Generating keys");
            keyGenerator.getKeys();
            log.info("Saving keys to file");
            keyGenerator.saveToFile(publicKeyPath, keyGenerator.getPublicKey().getEncoded());
            keyGenerator.saveToFile(privateKeyPath, keyGenerator.getPrivateKey().getEncoded());

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

    }

}
