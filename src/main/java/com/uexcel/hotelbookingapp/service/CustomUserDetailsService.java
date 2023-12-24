package com.uexcel.hotelbookingapp.service;

import com.uexcel.hotelbookingapp.entity.LoginCredentials;
import com.uexcel.hotelbookingapp.repository.LoginCredentialsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final LoginCredentialsRepository loginCredentialsRepository;

    public CustomUserDetailsService(LoginCredentialsRepository loginCredentialsRepository) {
        this.loginCredentialsRepository = loginCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("*********************"+username);
        LoginCredentials loginCredentials = loginCredentialsRepository.findByEmail(username);
        System.out.println("*****************************************"+loginCredentials.getPassword());

        if(loginCredentials == null){
            throw new UsernameNotFoundException("User not found");
        }

        return loginCredentials;
    }
}
