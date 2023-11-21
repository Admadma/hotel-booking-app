package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.RoleModel;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {


//    @Test
//    public void createRoleIfNotFoundShouldReturn(){
//        //todo: refactor  services to repositoryService
//        when(roleRepository.findRoleByName(TEST_ROLE)).thenReturn(Optional.of(Role.builder().name(TEST_ROLE).build()));
//    }
}
