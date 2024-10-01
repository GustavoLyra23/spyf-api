package com.gustavolyra.spy_price_finder.config;

import com.gustavolyra.spy_price_finder.repository.UserRepository;
import com.gustavolyra.spy_price_finder.service.util.TokenGeneratorUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final TokenGeneratorUtil tokenGeneratorUtil;

    public SecurityFilter(UserRepository userRepository, TokenGeneratorUtil tokenGeneratorUtil) {
        this.userRepository = userRepository;
        this.tokenGeneratorUtil = tokenGeneratorUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = getTokenFromRequest(request);
        var emailFromToken = tokenGeneratorUtil.validateToken(token);
        if (token != null && emailFromToken != null) {
            var user = userRepository.findByEmail(emailFromToken)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            var authorities = user.getAuthorities();
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        }
        return token.replace("Bearer ", "");
    }


}
