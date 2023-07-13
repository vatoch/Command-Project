package com.app.myproject.controller;

import com.app.myproject.annotations.TimeLog;
import com.app.myproject.entity.Command;
import com.app.myproject.param.CommandParam;
import com.app.myproject.repo.CommandRepository;
import com.app.myproject.service.CommandService;
import com.app.myproject.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/command")
@RequiredArgsConstructor
public class CommandController {
    private final TransactionService transactionService;
    private final CommandService commandService;
    @PostMapping("/buy/{id}")
    @TimeLog
    public ResponseEntity<Void> buyCommand(@RequestHeader(name = "username") String username,@PathVariable String id) {
        transactionService.buyCommand(username,id);
        return ResponseEntity.ok().build();

    }
    @PostMapping("/add")
    public ResponseEntity<Void> addCommand(@RequestBody CommandParam command) {
        commandService.addCommand(command);
        return ResponseEntity.ok().build();
    }

}
