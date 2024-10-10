package com.example.budgetingapp.security.services.impl;

import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                "Can't find user by email " + email));
    }
}
