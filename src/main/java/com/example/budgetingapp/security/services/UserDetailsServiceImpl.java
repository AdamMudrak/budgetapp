package com.example.budgetingapp.security.services;

import com.example.budgetingapp.exceptions.EntityNotFoundException;
import com.example.budgetingapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(()
                -> new EntityNotFoundException(
                "Can't find user by username " + username));
    }
}
