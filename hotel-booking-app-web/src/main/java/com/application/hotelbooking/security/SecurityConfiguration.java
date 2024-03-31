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
                    auth.requestMatchers("/hotelbooking/home/**", "/hotelbooking/newHome/**", "/hotelbooking/search-rooms/**", "/hotelbooking/search-rooms-new/**", "/hotelbooking/reserveroomNew/**", "/hotelbooking/register/**", "/error/**").permitAll();
                    auth.requestMatchers("/hotelbooking/admin/**").hasAnyAuthority("ADMIN");
                    auth.requestMatchers("/hotelbooking/rooms/**", "/hotelbooking/reservation/**").hasAnyAuthority("USER");
                    auth.requestMatchers("/hotelbooking/account/**", "/hotelbooking/change-password/**").hasAnyAuthority("ADMIN", "USER");
                    auth.requestMatchers("/images/*", "/myImages/**", "/hotelbooking/default", "/hotelbooking/login/**").permitAll();
                    auth.anyRequest().hasAnyAuthority("USER");
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
