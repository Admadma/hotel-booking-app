package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.repositories.RoleRepository;
import com.application.hotelbooking.services.implementations.RoleServiceImpl;
import com.application.hotelbooking.transformers.RoleTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    public static final String TEST_ROLE = "TestRole";
    public static final RoleModel TEST_ROLE_MODEL = RoleModel.builder().name(TEST_ROLE).build();
    public static final List<String> ROLE_NAMES = List.of("NAME1", "NAME3");
    public static final List<Role> ROLES = List.of(Role.builder().name("NAME1").build(), Role.builder().name("NAME2").build(), Role.builder().name("NAME3").build());
    public static final List<Role> SELECTED_ROLES = List.of(Role.builder().name("NAME1").build(), Role.builder().name("NAME3").build());
    public static final List<RoleModel> SELECTED_ROLE_MODELS = List.of(RoleModel.builder().name("NAME1").build(), RoleModel.builder().name("NAME3").build());
    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleTransformer roleTransformer;

//    @Test
//    public void createRoleIfNotFoundShouldReturn(){
//        //todo: refactor  services to repositoryService
//        when(roleRepository.findRoleByName(TEST_ROLE)).thenReturn(Optional.of(Role.builder().name(TEST_ROLE).build()));
//    }

    @Test
    public void testGetRolesShouldReturnCollectionOfRolesWhereNameIsInTheList(){
        when(roleRepository.findAll()).thenReturn(ROLES);
        when(roleTransformer.transformToRoleModels(SELECTED_ROLES)).thenReturn(SELECTED_ROLE_MODELS);

        Collection<RoleModel> roleModels = roleService.getRoles(ROLE_NAMES);

        verify(roleRepository).findAll();
        verify(roleTransformer).transformToRoleModels(SELECTED_ROLES);
        Assertions.assertThat(roleModels).isNotNull();
        Assertions.assertThat(roleModels).isEqualTo(SELECTED_ROLE_MODELS);
    }

    @Test
    public void testGetRolesSimplerShouldReturnCollectionOfRolesWhereNameIsInTheList(){
        when(roleRepository.findByNameIn(ROLE_NAMES)).thenReturn(SELECTED_ROLES);
        when(roleTransformer.transformToRoleModels(SELECTED_ROLES)).thenReturn(SELECTED_ROLE_MODELS);

        Collection<RoleModel> roleModels = roleService.getRolesSimpler(ROLE_NAMES);

        verify(roleRepository).findByNameIn(ROLE_NAMES);
        verify(roleTransformer).transformToRoleModels(SELECTED_ROLES);
        Assertions.assertThat(roleModels).isNotNull();
        Assertions.assertThat(roleModels).isEqualTo(SELECTED_ROLE_MODELS);
    }
}
