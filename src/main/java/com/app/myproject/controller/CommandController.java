package com.app.myproject.controller;

import com.app.myproject.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buy")
@RequiredArgsConstructor
public class CommandController {
    private final TransactionService transactionService;

    @PostMapping("/command/{commandname}")
    public ResponseEntity<Void> buyCommand(@RequestHeader(name = "username") String username, @PathVariable("commandname") String commandName) {
        transactionService.buyCommand(username,commandName);
        return ResponseEntity.ok().build();

    }

}
