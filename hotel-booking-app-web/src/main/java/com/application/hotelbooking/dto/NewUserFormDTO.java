package com.application.hotelbooking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserFormDTO {
    @Size(min = 8, max = 18, message = "{registration.error.username.length}")
    private String username;
    @Size(min = 8, max = 18, message = "{registration.error.password.length}")
    private String password;
    @NotEmpty(message = "{registration.error.email.empty}")
    private String email;
}
