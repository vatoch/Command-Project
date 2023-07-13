package com.app.myproject.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {

    public String username;
    public String phone;
    public String email;
    public BigDecimal balance;

}
