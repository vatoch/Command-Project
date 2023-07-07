package com.app.myproject.controller;

import com.app.myproject.annotations.TimeLog;
import com.app.myproject.entity.Command;
import com.app.myproject.repo.CommandRepository;
import com.app.myproject.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command")
@RequiredArgsConstructor
public class CommandController {
    private final TransactionService transactionService;
    private final CommandRepository commandRepository;

    @PostMapping("/buy/{commandname}")
    @TimeLog
    public ResponseEntity<Void> buyCommand(@RequestHeader(name = "username") String username, @PathVariable("commandname") String commandName) {
        transactionService.buyCommand(username,commandName);
        return ResponseEntity.ok().build();


    }
    @PostMapping("/add")
    public ResponseEntity<Void> addCommand(@RequestBody Command command) {
        commandRepository.save(command);
        return ResponseEntity.ok().build();
    }

}
