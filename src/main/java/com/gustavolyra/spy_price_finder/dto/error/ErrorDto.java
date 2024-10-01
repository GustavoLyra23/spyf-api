package com.gustavolyra.spy_price_finder.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorDto {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;


}
