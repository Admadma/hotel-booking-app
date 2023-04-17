package com.application.hotelbooking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotEmpty(message = "Please enter a username")
    private String username;
    @NotEmpty(message = "Please enter a password")
    @Size(min = 8, max = 25)
    private String password;
}
