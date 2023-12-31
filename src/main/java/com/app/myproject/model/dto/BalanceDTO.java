package com.app.myproject.model.dto;

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
