package com.app.myproject.controller;

import com.app.myproject.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService service;

    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestHeader(name = "username") String username) throws MessagingException {
        service.sendEmail(username,"vwtfsspgxzfhwdfl","frankiemvm@gmail.com","gamarjoba","zxc");

        return ResponseEntity.ok().build();
    }

}
