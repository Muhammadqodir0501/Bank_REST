package com.example.bankcards.service.abstractions;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
    UserDetails findUserByUsername(String username);
}
