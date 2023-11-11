package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

    private Long id;
    private Long version;
    private String username;
    private String password;
    private String email;
    private Boolean locked = false;
    private Boolean enabled = false;
    private List<ReservationView> reservations;
    private Collection<RoleView> roles;
}
