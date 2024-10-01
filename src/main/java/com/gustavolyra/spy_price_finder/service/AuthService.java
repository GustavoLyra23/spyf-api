package com.gustavolyra.spy_price_finder.service;

import com.gustavolyra.spy_price_finder.dto.user.UserDto;
import com.gustavolyra.spy_price_finder.repository.UserRepository;
import com.gustavolyra.spy_price_finder.service.exceptions.UnathorizedException;
import com.gustavolyra.spy_price_finder.service.util.TokenGeneratorUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenGeneratorUtil tokenGeneratorUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, TokenGeneratorUtil tokenGeneratorUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenGeneratorUtil = tokenGeneratorUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public String login(UserDto userDto) {
        var user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (passwordEncoder.matches(passwordEncoder.encode(userDto.getPassword()), user.getPassword())) {
            return tokenGeneratorUtil.generateToken(user);
        }

        throw new UnathorizedException("Invalid Credentials");
    }


}
