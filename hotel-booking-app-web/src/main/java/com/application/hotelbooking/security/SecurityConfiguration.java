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
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hotelbooking/home/**", "/hotelbooking/register/**", "/error/**").permitAll();
//                    auth.requestMatchers("/hotelbooking/admin/**").hasAnyAuthority("ADMIN");
                    auth.requestMatchers("/hotelbooking/admin/**").permitAll(); //TODO: REMOVE WHEN DONE WITH ADMIN PAGE DEVELOPMENT
                    auth.requestMatchers("/hotelbooking/rooms/**", "/hotelbooking/reservation/**").hasAnyAuthority("USER");
                    auth.requestMatchers("/images/*").permitAll();
//                    auth.anyRequest().hasAnyAuthority("USER");
                    auth.anyRequest().permitAll(); //TODO: REMOVE WHEN DONE WITH DEBUGGING
                 })
                .formLogin(form -> form
                        .loginPage("/hotelbooking/login")
                        .defaultSuccessUrl("/hotelbooking/home")
                        .permitAll())
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/hotelbooking/home");
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
