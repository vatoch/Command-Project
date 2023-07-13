package com.app.myproject.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {

    public BigDecimal money;
    public LocalDateTime lastUpdated;


}
