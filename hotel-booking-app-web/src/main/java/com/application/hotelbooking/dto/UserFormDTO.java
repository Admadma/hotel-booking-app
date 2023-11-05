package com.application.hotelbooking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFormDTO {
    @NotEmpty(message = "Please enter a username")
    private String username;
    @NotEmpty(message = "Please enter a password")
    @Size(min = 1, max = 25) // TODO: change back to 8
    private String password;
}
