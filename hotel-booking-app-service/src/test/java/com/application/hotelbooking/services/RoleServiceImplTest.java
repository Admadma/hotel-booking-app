package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.services.implementations.RoleServiceImpl;
import com.application.hotelbooking.services.repositoryservices.RoleRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    public static final String ROLE_NAME = "Test_role_name";
    public static final RoleModel ROLE_MODEL = RoleModel.builder().name(ROLE_NAME).build();
    public static final Optional<RoleModel> OPTIONAL_ROLE_MODEL = Optional.of(ROLE_MODEL);
    public static final Optional<RoleModel> EMPTY_OPTIONAL_ROLE_MODEL = Optional.empty();
    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepositoryService roleRepositoryService;

    @Test
    public void createRoleIfNotFoundShouldReturnCreatedRoleIfNotExistsAlready(){
        when(roleRepositoryService.getRoleByName(ROLE_NAME)).thenReturn(EMPTY_OPTIONAL_ROLE_MODEL);
        when(roleRepositoryService.saveRole(ROLE_MODEL)).thenReturn(ROLE_MODEL);

        RoleModel savedRole = roleService.createRoleIfNotFound(ROLE_NAME);

        verify(roleRepositoryService).getRoleByName(ROLE_NAME);
        verify(roleRepositoryService).saveRole(ROLE_MODEL);
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getName()).isEqualTo(ROLE_NAME);
    }

    @Test
    public void createRoleIfNotFoundShouldReturnExistingRoleIfAlreadyExists(){
        when(roleRepositoryService.getRoleByName(ROLE_NAME)).thenReturn(OPTIONAL_ROLE_MODEL);

        RoleModel savedRole = roleService.createRoleIfNotFound(ROLE_NAME);

        verify(roleRepositoryService).getRoleByName(ROLE_NAME);
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getName()).isEqualTo(ROLE_NAME);
    }
}
