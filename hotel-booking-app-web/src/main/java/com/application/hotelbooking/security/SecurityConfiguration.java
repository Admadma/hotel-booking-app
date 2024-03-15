package com.application.hotelbooking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
                    auth.requestMatchers("/hotelbooking/home/**", "/hotelbooking/search-rooms/**", "/hotelbooking/register/**", "/error/**").permitAll();
                    auth.requestMatchers("/hotelbooking/admin/**").hasAnyAuthority("ADMIN");
                    auth.requestMatchers("/hotelbooking/rooms/**", "/hotelbooking/reservation/**").hasAnyAuthority("USER");
                    auth.requestMatchers("/images/*").permitAll();
                    auth.anyRequest().hasAnyAuthority("USER");
                 })
                .formLogin(form -> form
                        .loginPage("/hotelbooking/login")
                        .defaultSuccessUrl("/hotelbooking/home")
                        .permitAll())
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/hotelbooking/login?logout");
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
