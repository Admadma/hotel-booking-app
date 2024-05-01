package com.application.hotelbooking.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeCredentialsDto {

    @Size(min = 8, max = 18, message = "{registration.error.password.length}")
    private String oldPassword;
    @Size(min = 8, max = 18, message = "{registration.error.password.length}")
    private String newPassword;
}
