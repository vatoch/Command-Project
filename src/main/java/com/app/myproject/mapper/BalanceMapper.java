package com.app.myproject.mapper;

import com.app.myproject.model.dto.BalanceDTO;
import com.app.myproject.entity.Balance;

public class BalanceMapper {


    public static BalanceDTO balanceToDTO(Balance balance) {
        return BalanceDTO.builder()
                .money(balance.getMoney())
                .lastUpdated(balance.getLastUpdated())
                .build();
    }

}
