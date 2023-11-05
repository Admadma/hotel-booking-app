package com.application.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private Long version;
    private String username;
    private String password;
    private List<ReservationDTO> reservations;
    private Collection<RoleDTO> roles;
}
