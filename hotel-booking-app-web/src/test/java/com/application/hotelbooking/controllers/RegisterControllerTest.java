package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.NewUserFormDTO;
import com.application.hotelbooking.exceptions.EmailAlreadyExistsException;
import com.application.hotelbooking.exceptions.UserAlreadyExistsException;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.UserService;
import com.application.hotelbooking.validators.InternetAddressValidator;
import jakarta.mail.internet.AddressException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Import(SecurityConfiguration.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {

    private static final List<String> SINGLETON_LIST_OF_USER_ROLE = List.of("USER");
    private static final NewUserFormDTO NEW_USER_FORM_DTO = new NewUserFormDTO("Username", "password", "test@email.com");
    private static final NewUserFormDTO EMPTY_NEW_USER_FORM_DTO = new NewUserFormDTO();
    private static final NewUserFormDTO NEW_USER_FORM_DTO_WITH_THREE_INVALID_FIELDS = new NewUserFormDTO("", "", "");
    private static final NewUserFormDTO NEW_USER_FORM_DTO_INVALID_EMAIL = new NewUserFormDTO("Username", "password", "invalid");

    @MockBean
    private UserService userService;

    @MockBean
    private InternetAddressValidator internetAddressValidator;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnauthenticatedUserCanNavigateToRegisterPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("newUserFormDTO"))
                .andExpect(model().attribute("newUserFormDTO", EMPTY_NEW_USER_FORM_DTO));
    }

    @Test
    public void testCreateNewUserShouldReturnToRegisterPageWithErrorIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/register/create-new-user")
                        .flashAttr("newUserFormDTO", NEW_USER_FORM_DTO_WITH_THREE_INVALID_FIELDS))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("newUserFormDTO", NEW_USER_FORM_DTO_WITH_THREE_INVALID_FIELDS))
                .andExpect(model().attributeErrorCount("newUserFormDTO", 3));
    }

    @Test
    public void testCreateNewUserShouldRejectInvalidEmailAndReturnToRegisterPageWithErrorCode() throws Exception {
        doThrow(AddressException.class).when(internetAddressValidator).validate(NEW_USER_FORM_DTO_INVALID_EMAIL.getEmail());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/register/create-new-user")
                        .flashAttr("newUserFormDTO", NEW_USER_FORM_DTO_INVALID_EMAIL))
                .andExpect(status().isOk())
                .andExpect(model().attribute("newUserFormDTO", NEW_USER_FORM_DTO_INVALID_EMAIL))
                .andExpect(model().attributeHasFieldErrorCode("newUserFormDTO", "email", "registration.error.email.invalid"))
                .andExpect(view().name("register"));

        verify(internetAddressValidator).validate(NEW_USER_FORM_DTO_INVALID_EMAIL.getEmail());
    }

    @Test
    public void testCreateNewUserShouldRejectUsernameIfItsTakenAndReturnToRegisterPageWithErrorCode() throws Exception {
        doNothing().when(internetAddressValidator).validate(NEW_USER_FORM_DTO.getEmail());
        doThrow(UserAlreadyExistsException.class).when(userService).createUser(NEW_USER_FORM_DTO.getUsername(), NEW_USER_FORM_DTO.getPassword(), NEW_USER_FORM_DTO.getEmail(), SINGLETON_LIST_OF_USER_ROLE);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/register/create-new-user")
                        .flashAttr("newUserFormDTO", NEW_USER_FORM_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attribute("newUserFormDTO", NEW_USER_FORM_DTO))
                .andExpect(model().attributeHasFieldErrorCode("newUserFormDTO", "username", "registration.error.username.taken"))
                .andExpect(view().name("register"));

        verify(internetAddressValidator).validate(NEW_USER_FORM_DTO.getEmail());
        verify(userService).createUser(NEW_USER_FORM_DTO.getUsername(), NEW_USER_FORM_DTO.getPassword(), NEW_USER_FORM_DTO.getEmail(), SINGLETON_LIST_OF_USER_ROLE);
    }

    @Test
    public void testCreateNewUserShouldRejectEmailIfItsTakenAndReturnToRegisterPageWithErrorCode() throws Exception {
        doNothing().when(internetAddressValidator).validate(NEW_USER_FORM_DTO.getEmail());
        doThrow(EmailAlreadyExistsException.class).when(userService).createUser(NEW_USER_FORM_DTO.getUsername(), NEW_USER_FORM_DTO.getPassword(), NEW_USER_FORM_DTO.getEmail(), SINGLETON_LIST_OF_USER_ROLE);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/register/create-new-user")
                        .flashAttr("newUserFormDTO", NEW_USER_FORM_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attribute("newUserFormDTO", NEW_USER_FORM_DTO))
                .andExpect(model().attributeHasFieldErrorCode("newUserFormDTO", "email", "registration.error.email.taken"))
                .andExpect(view().name("register"));

        verify(internetAddressValidator).validate(NEW_USER_FORM_DTO.getEmail());
        verify(userService).createUser(NEW_USER_FORM_DTO.getUsername(), NEW_USER_FORM_DTO.getPassword(), NEW_USER_FORM_DTO.getEmail(), SINGLETON_LIST_OF_USER_ROLE);
    }

    @Test
    public void testCreateNewUserShouldReturnToRegisterPageWithErrorCodeIfAnyOtherExceptionOccurred() throws Exception {
        doNothing().when(internetAddressValidator).validate(NEW_USER_FORM_DTO.getEmail());
        doThrow(DataIntegrityViolationException.class).when(userService).createUser(NEW_USER_FORM_DTO.getUsername(), NEW_USER_FORM_DTO.getPassword(), NEW_USER_FORM_DTO.getEmail(), SINGLETON_LIST_OF_USER_ROLE);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/register/create-new-user")
                        .flashAttr("newUserFormDTO", NEW_USER_FORM_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attribute("newUserFormDTO", NEW_USER_FORM_DTO))
                .andExpect(model().attributeHasErrors("newUserFormDTO"))
                .andExpect(view().name("register"));

        verify(internetAddressValidator).validate(NEW_USER_FORM_DTO.getEmail());
        verify(userService).createUser(NEW_USER_FORM_DTO.getUsername(), NEW_USER_FORM_DTO.getPassword(), NEW_USER_FORM_DTO.getEmail(), SINGLETON_LIST_OF_USER_ROLE);
    }

    @Test
    public void testCreateNewUserShouldSetSessionAttributeAndRedirectToConfirmEmailPageIfNoExceptionsOccurred() throws Exception {
        doNothing().when(internetAddressValidator).validate(NEW_USER_FORM_DTO.getEmail());
        when(userService.createUser(NEW_USER_FORM_DTO.getUsername(), NEW_USER_FORM_DTO.getPassword(), NEW_USER_FORM_DTO.getEmail(), SINGLETON_LIST_OF_USER_ROLE)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/register/create-new-user")
                        .flashAttr("newUserFormDTO", NEW_USER_FORM_DTO))
                .andExpect(request().sessionAttribute("email", NEW_USER_FORM_DTO.getEmail()))
                .andExpect(redirectedUrl("/hotelbooking/register/confirm-email"));

        verify(internetAddressValidator).validate(NEW_USER_FORM_DTO.getEmail());
        verify(userService).createUser(NEW_USER_FORM_DTO.getUsername(), NEW_USER_FORM_DTO.getPassword(), NEW_USER_FORM_DTO.getEmail(), SINGLETON_LIST_OF_USER_ROLE);
    }
}
