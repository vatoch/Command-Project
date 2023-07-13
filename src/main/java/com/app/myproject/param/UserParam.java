package com.app.myproject.param;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserParam {

    private String name;
    private String email;
    private String phone;

}
