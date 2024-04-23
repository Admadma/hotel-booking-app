package com.application.hotelbooking.transformers;

import com.application.hotelbooking.entities.Role;
import com.application.hotelbooking.domain.RoleModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleTransformerTest {
    public static final Class<Role> ROLE_CLASS = Role.class;
    public static final Role ROLE = new Role();
    public static final Optional<Role> OPTIONAL_ROLE = Optional.of(ROLE);
    public static final Optional<Role> EMPTY_OPTIONAL_ROLE = Optional.empty();
    public static final Collection<Role> ROLES = List.of(ROLE);
    public static final Class<RoleModel> ROLE_MODEL_CLASS = RoleModel.class;
    public static final RoleModel ROLE_MODEL = new RoleModel();
    public static final Optional<RoleModel> OPTIONAL_ROLE_MODEL = Optional.of(ROLE_MODEL);

    public static final Collection<RoleModel> ROLE_MODELS = List.of(ROLE_MODEL);


    @InjectMocks
    private RoleTransformer roleTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToRoleShouldReturnTransformedRoleModel(){
        when(modelMapper.map(ROLE_MODEL, ROLE_CLASS)).thenReturn(ROLE);

        Role resultRole = roleTransformer.transformToRole(ROLE_MODEL);

        verify(modelMapper).map(ROLE_MODEL, ROLE_CLASS);
        Assertions.assertThat(resultRole).isNotNull();
        Assertions.assertThat(resultRole).isEqualTo(ROLE);
    }

    @Test
    public void testTransformToRoleModelShouldReturnTransformedRole(){
        when(modelMapper.map(ROLE, ROLE_MODEL_CLASS)).thenReturn(ROLE_MODEL);

        RoleModel resultRoleModel = roleTransformer.transformToRoleModel(ROLE);

        verify(modelMapper).map(ROLE, ROLE_MODEL_CLASS);
        Assertions.assertThat(resultRoleModel).isNotNull();
        Assertions.assertThat(resultRoleModel).isEqualTo(ROLE_MODEL);
    }

    @Test
    public void testTransformToOptionalRoleModelShouldReturnTransformedRoleIfRolePresent(){
        when(modelMapper.map(OPTIONAL_ROLE, ROLE_MODEL_CLASS)).thenReturn(ROLE_MODEL);

        Optional<RoleModel> resultRoleModel = roleTransformer.transformToOptionalRoleModel(OPTIONAL_ROLE);

        verify(modelMapper).map(OPTIONAL_ROLE, ROLE_MODEL_CLASS);
        Assertions.assertThat(resultRoleModel).isNotNull();
        Assertions.assertThat(resultRoleModel).isNotEmpty();
        Assertions.assertThat(resultRoleModel).isEqualTo(OPTIONAL_ROLE_MODEL);
    }

    @Test
    public void testTransformToOptionalRoleModelShouldReturnEmptyOptionalIfRolePresent(){

        Optional<RoleModel> resultRoleModel = roleTransformer.transformToOptionalRoleModel(EMPTY_OPTIONAL_ROLE);

        Assertions.assertThat(resultRoleModel).isNotNull();
        Assertions.assertThat(resultRoleModel).isEmpty();
    }

    @Test
    public void testTransformToRoleModelsShouldReturnCollectionOfTransformedRoles(){
        when(modelMapper.map(ROLE, ROLE_MODEL_CLASS)).thenReturn(ROLE_MODEL);

        Collection<RoleModel> resultRoleModels = roleTransformer.transformToRoleModels(ROLES);

        verify(modelMapper).map(ROLE, ROLE_MODEL_CLASS);
        Assertions.assertThat(resultRoleModels).isNotNull();
        Assertions.assertThat(resultRoleModels).isNotEmpty();
        Assertions.assertThat(resultRoleModels).isEqualTo(ROLE_MODELS);
    }
}
