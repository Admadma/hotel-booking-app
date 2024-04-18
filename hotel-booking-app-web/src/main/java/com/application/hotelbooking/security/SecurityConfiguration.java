package com.application.hotelbooking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hotelbooking/admin/**").hasAnyAuthority("ADMIN");
                    auth.requestMatchers("/hotelbooking/reserveroom/**", "/hotelbooking/reserve/**", "/hotelbooking/review/**").hasAnyAuthority("USER");
                    auth.requestMatchers("/hotelbooking/default", "/hotelbooking/account/**", "/hotelbooking/change-password/**").authenticated();
                    auth.requestMatchers("/hotelbooking/login/**", "/hotelbooking/register/**", "/error/**", "/images/**", "/hotelbooking/home/**", "/hotelbooking/search-rooms/**").permitAll();
                })
                .formLogin(form -> form
                        .loginPage("/hotelbooking/login")
                        .defaultSuccessUrl("/hotelbooking/default")
                        .permitAll())
                .logout()
                .logoutUrl("/logout");
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
