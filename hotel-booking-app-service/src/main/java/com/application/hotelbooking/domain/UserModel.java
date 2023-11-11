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
public class UserModel {

    private Long id;
    private Long version;
    private String username;
    private String password;
    private String email;
    @Builder.Default
    private Boolean locked = false;
    @Builder.Default
    private Boolean enabled = false;
    private List<ReservationModel> reservations;
    private Collection<RoleModel> roles;
}
