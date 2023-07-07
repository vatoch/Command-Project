package com.app.myproject.exceptionhandling;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionBody {

    private String message;


}
