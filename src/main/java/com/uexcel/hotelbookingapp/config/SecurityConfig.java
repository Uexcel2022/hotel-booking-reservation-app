package com.uexcel.hotelbookingapp.config;

import com.uexcel.hotelbookingapp.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/user").permitAll()
                        .requestMatchers("/rooms").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/calendar").permitAll()
                        .requestMatchers("/booking").permitAll()
                        .requestMatchers("/**.css").permitAll()
                        .requestMatchers("/admin").hasAuthority("ADMIN")
                        .requestMatchers("/reservations").hasAuthority("ADMIN")
                        .requestMatchers("/delete").hasAuthority("ADMIN")
                        .requestMatchers("/book_checkin").hasAuthority("ADMIN")
                        .requestMatchers("/checkin").hasAuthority("ADMIN")
                        .requestMatchers("/check_in").hasAuthority("ADMIN")
                        .requestMatchers("/add-room").hasAuthority("ADMIN")
                        .requestMatchers("/booked-room").hasAuthority("ADMIN")
                        .requestMatchers("/room-usage").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form->form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/admin").permitAll());

        return http.build();
    }

    @Autowired
    public void authenticationBuilder(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder(11));
    }
}
