package com.gustavolyra.spy_price_finder.controller;

import com.gustavolyra.spy_price_finder.dto.user.UserDto;
import com.gustavolyra.spy_price_finder.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserDto userDto) {
        var token = authService.login(userDto);
        return ResponseEntity.ok(Map.of("token", token));
    }

}
