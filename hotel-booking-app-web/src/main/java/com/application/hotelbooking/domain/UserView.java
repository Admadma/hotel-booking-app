package com.application.hotelbooking.domain;

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
public class UserView {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean locked = false;
    private Boolean enabled = false;
    private List<ReservationView> reservations;
    private Collection<RoleView> roles;
}
