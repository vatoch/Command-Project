package com.app.myproject.model.param;

import com.app.myproject.model.enums.CommandType;
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
