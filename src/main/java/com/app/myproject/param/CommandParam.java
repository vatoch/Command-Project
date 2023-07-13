package com.app.myproject.param;

import com.app.myproject.enums.CommandType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommandParam {
    private String name;
    private BigDecimal price;
    private CommandType type;


}
