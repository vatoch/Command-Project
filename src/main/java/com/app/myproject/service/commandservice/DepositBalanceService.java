package com.app.myproject.service.commandservice;

import com.app.myproject.service.BalanceService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositBalanceService implements GenericService{
    private final BalanceService balanceService;
    private static final String name = "Deposit money";
    @Override
    public void execute(String... args) {
        balanceService.fillBalance(args[0],args[3].replace(".",""));
    }

    @Override
    public String getName() {
        return name;
    }

}
