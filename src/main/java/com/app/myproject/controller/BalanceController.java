package com.app.myproject.controller;

import com.app.myproject.service.BalanceService;
import com.app.myproject.service.TransactionService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;
    private final TransactionService transactionService;

    @PostMapping("/transfer/{receiver}/{amount}")
    public ResponseEntity<Void> transferMoney(@RequestHeader(name = "username") String sender, @PathVariable String receiver,@PathVariable String amount) {
        System.out.println(amount);
        BigDecimal bigDecimal = new BigDecimal(amount);
        transactionService.transferMoney(sender,receiver,bigDecimal);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/fill/{amount}")
    public ResponseEntity<Void> fillBalance(@RequestHeader(name = "username") String username,@PathVariable BigDecimal amount) {
        balanceService.fillBalance(username,amount);
        return ResponseEntity.ok().build();

    }


}
