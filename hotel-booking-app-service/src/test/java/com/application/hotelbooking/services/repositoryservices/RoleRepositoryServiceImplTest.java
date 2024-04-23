package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.entities.Role;
import com.application.hotelbooking.models.RoleModel;
import com.application.hotelbooking.repositories.RoleRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.RoleRepositoryServiceImpl;
import com.application.hotelbooking.transformers.RoleTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryServiceImplTest {
    private static final String ROLE_NAME = "TestRole";
    private static final Role ROLE = Role.builder().name(ROLE_NAME).build();
    private static final Optional<Role> OPTIONAL_ROLE = Optional.of(ROLE);
    private static final Optional<Role> EMPTY_OPTIONAL_ROLE = Optional.empty();
    private static final RoleModel ROLE_MODEL = RoleModel.builder().name(ROLE_NAME).build();
    private static final Optional<RoleModel> OPTIONAL_ROLE_MODEL = Optional.of(ROLE_MODEL);
    private static final Optional<RoleModel> EMPTY_OPTIONAL_ROLE_MODEL = Optional.empty();
    private static final List<String> ROLE_NAMES = List.of("NAME1", "NAME3");
    private static final List<Role> SELECTED_ROLES = List.of(Role.builder().name("NAME1").build(), Role.builder().name("NAME3").build());
    private static final List<RoleModel> SELECTED_ROLE_MODELS = List.of(RoleModel.builder().name("NAME1").build(), RoleModel.builder().name("NAME3").build());
    @InjectMocks
    private RoleRepositoryServiceImpl roleRepositoryService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleTransformer roleTransformer;

    @Test
    public void testGetRoleByNameShouldReturnOptionalOfRoleModelIfRoleExists(){
        when(roleRepository.findRoleByName(ROLE_NAME)).thenReturn(OPTIONAL_ROLE);
        when(roleTransformer.transformToOptionalRoleModel(OPTIONAL_ROLE)).thenReturn(OPTIONAL_ROLE_MODEL);

        Optional<RoleModel> resultRoleModel = roleRepositoryService.getRoleByName(ROLE_NAME);

        verify(roleRepository).findRoleByName(ROLE_NAME);
        verify(roleTransformer).transformToOptionalRoleModel(OPTIONAL_ROLE);
        Assertions.assertThat(resultRoleModel).isNotNull();
        Assertions.assertThat(resultRoleModel).isNotEmpty();
        Assertions.assertThat(resultRoleModel.get().getName()).isEqualTo(ROLE_NAME);
    }

    @Test
    public void testGetRoleByNameShouldReturnEmptyOptionalIfRoleDoesNotExist(){
        when(roleRepository.findRoleByName(ROLE_NAME)).thenReturn(EMPTY_OPTIONAL_ROLE);
        when(roleTransformer.transformToOptionalRoleModel(EMPTY_OPTIONAL_ROLE)).thenReturn(EMPTY_OPTIONAL_ROLE_MODEL);

        Optional<RoleModel> resultRoleModel = roleRepositoryService.getRoleByName(ROLE_NAME);

        verify(roleRepository).findRoleByName(ROLE_NAME);
        verify(roleTransformer).transformToOptionalRoleModel(EMPTY_OPTIONAL_ROLE);
        Assertions.assertThat(resultRoleModel).isNotNull();
        Assertions.assertThat(resultRoleModel).isEmpty();
    }

    @Test
    public void testGetRolesShouldReturnCollectionOfRolesWhereNameIsInTheList(){
        when(roleRepository.findByNameIn(ROLE_NAMES)).thenReturn(SELECTED_ROLES);
        when(roleTransformer.transformToRoleModels(SELECTED_ROLES)).thenReturn(SELECTED_ROLE_MODELS);

        Collection<RoleModel> roleModels = roleRepositoryService.getRoles(ROLE_NAMES);

        verify(roleRepository).findByNameIn(ROLE_NAMES);
        verify(roleTransformer).transformToRoleModels(SELECTED_ROLES);
        Assertions.assertThat(roleModels).isNotNull();
        Assertions.assertThat(roleModels).isEqualTo(SELECTED_ROLE_MODELS);
    }

    @Test
    public void testSaveRoleShouldReturnRoleModelOfSavedRole(){
        when(roleTransformer.transformToRole(ROLE_MODEL)).thenReturn(ROLE);
        when(roleRepository.save(ROLE)).thenReturn(ROLE);
        when(roleTransformer.transformToRoleModel(ROLE)).thenReturn(ROLE_MODEL);

        RoleModel savedRoleModel = roleRepositoryService.saveRole(ROLE_MODEL);

        verify(roleTransformer).transformToRole(ROLE_MODEL);
        verify(roleRepository).save(ROLE);
        verify(roleTransformer).transformToRoleModel(ROLE);
        Assertions.assertThat(savedRoleModel).isNotNull();
        Assertions.assertThat(savedRoleModel.getName()).isEqualTo(ROLE_NAME);
    }
}
