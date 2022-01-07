package me.np99.application.controllers;

import me.np99.application.dtos.SecureMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SenderController {


    @PostMapping("/secure")
    ResponseEntity<String> getSecurityKeys(@RequestBody SecureMessage secureMessage){

        return ResponseEntity.status(HttpStatus.OK).body("Hello World");
    }
}
