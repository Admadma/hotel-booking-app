package com.application.hotelbooking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel {

    private Long id;
    private String name;
    private Collection<UserModel> users;
}
