package me.np99.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.np99.application.dtos.SecureMessage;
import me.np99.application.dtos.SecurityResponse;
import me.np99.application.utils.HmacUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SenderController {

    private final HmacUtil hmacUtil;

    @Autowired
    public SenderController(HmacUtil hmacUtil){
        this.hmacUtil = hmacUtil;
    }

    @PostMapping("/secure")
    ResponseEntity<SecurityResponse> getSecurityKeys(@RequestBody SecureMessage secureMessage){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String messageBody = objectMapper.writeValueAsString(secureMessage);

            String base64EncodedHmac = hmacUtil.generateHmac(messageBody);

            return ResponseEntity.status(HttpStatus.OK).body(new SecurityResponse(base64EncodedHmac));

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
