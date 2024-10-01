package com.gustavolyra.spy_price_finder.dto.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
public class ValidationErrorDto extends ErrorDto {

    private final Set<FieldErrorDto> fieldErrors = new HashSet<>();

    public ValidationErrorDto(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public void addFieldError(FieldErrorDto fieldError) {
        fieldErrors.add(fieldError);
    }


}
