package com.app.myproject.service.commandservice;

import com.app.myproject.service.TransactionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferMoneyService implements GenericService {

    private static final String name = "Transfer money";
    private final TransactionService transactionService;

    @Override
    public void execute(String... args) {
        transactionService.transferMoney(args[0],args[4],args[5]);
    }

    @Override
    public String getName() {
        return name;
    }

}
