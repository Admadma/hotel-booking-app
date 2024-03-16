package com.application.hotelbooking.security;

import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.UserModel;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.services.RoleService;
import com.application.hotelbooking.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SetupDataLoaderTest {

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "adminadmin";
    public static final String ADMIN_EMAIL = "hotelbookingservice01@gmail.com";
    public static final List<String> ADMIN_ROLES = List.of(ADMIN_ROLE);
    public static final RoleModel ROLE_MODEL = new RoleModel();
    public static final UserModel USER_MODEL = new UserModel();
    @InjectMocks
    private SetupDataLoader setupDataLoader;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Test
    public void testOnApplicationEventCatchesExceptionIfAdminUserAlreadyExists(){
        when(roleService.createRoleIfNotFound(ADMIN_ROLE)).thenReturn(ROLE_MODEL);
        when(roleService.createRoleIfNotFound(USER_ROLE)).thenReturn(ROLE_MODEL);
        doThrow(UserAlreadyExistsException.class).when(userService).createUser(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL, ADMIN_ROLES);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ContextRefreshedEvent event = new ContextRefreshedEvent(applicationContext);

        assertDoesNotThrow(() -> setupDataLoader.onApplicationEvent(event));

        verify(roleService).createRoleIfNotFound(ADMIN_ROLE);
        verify(roleService).createRoleIfNotFound(USER_ROLE);
        verify(userService).createUser(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL, ADMIN_ROLES);
        Assertions.assertTrue(setupDataLoader.alreadySetup);
    }

    @Test
    public void testOnApplicationEventCreatesAdminUserIfNotExists(){
        when(roleService.createRoleIfNotFound(ADMIN_ROLE)).thenReturn(ROLE_MODEL);
        when(roleService.createRoleIfNotFound(USER_ROLE)).thenReturn(ROLE_MODEL);
        when(userService.createUser(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL, ADMIN_ROLES)).thenReturn(USER_MODEL);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ContextRefreshedEvent event = new ContextRefreshedEvent(applicationContext);

        assertDoesNotThrow(() -> setupDataLoader.onApplicationEvent(event));

        verify(roleService).createRoleIfNotFound(ADMIN_ROLE);
        verify(roleService).createRoleIfNotFound(USER_ROLE);
        verify(userService).createUser(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL, ADMIN_ROLES);
        Assertions.assertTrue(setupDataLoader.alreadySetup);
    }
}
