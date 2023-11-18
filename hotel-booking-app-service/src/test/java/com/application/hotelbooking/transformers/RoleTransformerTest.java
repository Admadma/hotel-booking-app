package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Role;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleTransformerTest {
    public static final Class<Role> ROLE_CLASS = Role.class;
    public static final Role ROLE = new Role();
    public static final Collection<Role> ROLES = List.of(ROLE);
    public static final Class<RoleModel> ROLE_MODEL_CLASS = RoleModel.class;
    public static final RoleModel ROLE_MODEL = new RoleModel();
    public static final Collection<RoleModel> ROLE_MODELS = List.of(ROLE_MODEL);


    @InjectMocks
    private RoleTransformer roleTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToUserShouldReturnTransformedUserModel(){
        when(modelMapper.map(ROLE_MODEL, ROLE_CLASS)).thenReturn(ROLE);

        Role resultRole = roleTransformer.transformToRole(ROLE_MODEL);

        verify(modelMapper).map(ROLE_MODEL, ROLE_CLASS);
        Assertions.assertThat(resultRole).isNotNull();
        Assertions.assertThat(resultRole).isEqualTo(ROLE);
    }

    @Test
    public void testTransformToUserModelShouldReturnTransformedUser(){
        when(modelMapper.map(ROLE, ROLE_MODEL_CLASS)).thenReturn(ROLE_MODEL);

        RoleModel resultRoleModel = roleTransformer.transformToRoleModel(ROLE);

        verify(modelMapper).map(ROLE, ROLE_MODEL_CLASS);
        Assertions.assertThat(resultRoleModel).isNotNull();
        Assertions.assertThat(resultRoleModel).isEqualTo(ROLE_MODEL);
    }

    @Test
    public void testTransformToRoleModelsReturnsCollectionOfTransformedRoles(){
        when(modelMapper.map(ROLE, ROLE_MODEL_CLASS)).thenReturn(ROLE_MODEL);

        Collection<RoleModel> resultRoleModels = roleTransformer.transformToRoleModels(ROLES);

        verify(modelMapper).map(ROLE, ROLE_MODEL_CLASS);
        Assertions.assertThat(resultRoleModels).isNotNull();
        Assertions.assertThat(resultRoleModels).isNotEmpty();
        Assertions.assertThat(resultRoleModels).isEqualTo(ROLE_MODELS);
    }
}
