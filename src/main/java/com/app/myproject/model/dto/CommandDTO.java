package com.app.myproject.model.dto;


import com.app.myproject.model.enums.CommandType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommandDTO {
    private String name;
    private CommandType type;
    private BigDecimal price;

}
