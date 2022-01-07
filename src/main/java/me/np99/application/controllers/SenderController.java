package me.np99.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.np99.application.dtos.SecureMessage;
import me.np99.application.dtos.SecurityResponse;
import me.np99.application.utils.DigitalSignatureUtil;
import me.np99.application.utils.HmacUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SenderController {

    private final HmacUtil hmacUtil;
    private final DigitalSignatureUtil digitalSignatureUtil;

    @Autowired
    public SenderController(HmacUtil hmacUtil, DigitalSignatureUtil digitalSignatureUtil){
        this.hmacUtil = hmacUtil;
        this.digitalSignatureUtil = digitalSignatureUtil;
    }

    @PostMapping("/secure")
    ResponseEntity<SecurityResponse> getSecurityKeys(@RequestBody SecureMessage secureMessage){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String messageBody = objectMapper.writeValueAsString(secureMessage);

            String base64EncodedHmac = hmacUtil.generateHmac(messageBody);
            byte[] signature = digitalSignatureUtil.sign(messageBody);
            String base64EncodedSignature = Base64.encodeBase64String(signature);

            return ResponseEntity.status(HttpStatus.OK).body(new SecurityResponse(base64EncodedHmac,base64EncodedSignature));

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
