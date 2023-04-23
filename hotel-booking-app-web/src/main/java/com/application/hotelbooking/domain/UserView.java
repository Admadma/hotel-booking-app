package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

    private Long id;
    private Long version;
    private String username;
    private String password;
    private Collection<RoleView> roles;
}
