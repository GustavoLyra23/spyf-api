package com.gustavolyra.spy_price_finder.service.util;

import com.gustavolyra.spy_price_finder.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    private UserUtil() {
        throw new IllegalStateException("Utility class");
    }


    public static User findUserFromAuthenticationContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }


}
