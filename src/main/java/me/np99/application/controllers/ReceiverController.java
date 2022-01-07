package me.np99.application.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.np99.application.dtos.SecureMessage;
import me.np99.application.utils.HmacUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReceiverController {

    private final HmacUtil hmacUtil;

    @Autowired
    public ReceiverController(HmacUtil hmacUtil){
        this.hmacUtil = hmacUtil;
    }

    @PostMapping("/verify")
    ResponseEntity<String> verifyMessage(
            @RequestHeader String hmac,
            @RequestBody SecureMessage secureMessage){
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            String messageBody = objectMapper.writeValueAsString(secureMessage);
            String base64EncodedHmac = hmacUtil.generateHmac(messageBody);

            boolean hmacVerified = base64EncodedHmac.equals(hmac);

            boolean verified = hmacVerified;

            if(verified){
                log.info("Hmac Verified");
                return ResponseEntity.status(HttpStatus.OK).body(messageBody);
            }else{
                return ResponseEntity.status(HttpStatus.OK).body("Not verified");
            }

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
