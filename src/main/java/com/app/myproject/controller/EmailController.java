package com.app.myproject.controller;

import com.app.myproject.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService service;

    @PostMapping("/send/{receiver}")
    public ResponseEntity<Void> sendEmail(@RequestHeader(name = "username") String username,@PathVariable String receiver) throws MessagingException {
        service.sendEmail(username,receiver,"gamarjoba","zxc");

        return ResponseEntity.ok().build();
    }

}
