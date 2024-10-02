package com.gustavolyra.spy_price_finder.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDto {

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 6, message = "Password should have at least 6 characters")
    @NotBlank(message = "Password should not be blank")
    private String password;


}
