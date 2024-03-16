package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.HotelCreationDTO;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfiguration.class)
@WebMvcTest(AddHotelsController.class)
public class AddHotelsControllerTest {

    private HotelCreationDTO HOTEL_CREATION_DTO = new HotelCreationDTO("Test Hotel", "Test City");
    private HotelCreationDTO EMPTY_HOTEL_CREATION_DTO = new HotelCreationDTO();
    private HotelCreationServiceDTO HOTEL_CREATION_SERVICE_DTO = new HotelCreationServiceDTO("Test Hotel", "Test City");
    private HotelCreationDTO HOTEL_SHORT_NAME_HAS_CITY = new HotelCreationDTO("A", "City name");
    private HotelCreationDTO HOTEL_GOOD_NAME_NO_CITY = new HotelCreationDTO("Long name", "");

    @MockBean
    private HotelService hotelService;

    @MockBean
    private HotelViewTransformer hotelViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminUserCanNavigateToAddHotelsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/addHotels"))
                .andExpect(status().isOk())
                .andExpect(view().name("addhotels"))
                .andExpect(model().attributeExists("hotelCreationDTO"))
                .andExpect(model().attribute("hotelCreationDTO", EMPTY_HOTEL_CREATION_DTO));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testNonAdminUserForbiddenToNavigateToAddHotelsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/addHotels"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testCreateNewHotelForbiddenForNonAdminUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldReturnToAddHotelsPageWithErrorIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_SHORT_NAME_HAS_CITY))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(view().name("addhotels"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_SHORT_NAME_HAS_CITY))
                .andExpect(model().attributeHasErrors("hotelCreationDTO"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_GOOD_NAME_NO_CITY))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(view().name("addhotels"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_GOOD_NAME_NO_CITY))
                .andExpect(model().attributeHasErrors("hotelCreationDTO"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldRejectTakenHotelNameAndReturnToAddHotelsPageWithErrorCode() throws Exception {
        when(hotelViewTransformer.transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO)).thenReturn(HOTEL_CREATION_SERVICE_DTO);
        when(hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO)).thenThrow(InvalidHotelException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(model().attributeHasFieldErrorCode("hotelCreationDTO", "hotelName", "admin.hotel.validation.hotelname.taken"))
                .andExpect(view().name("addhotels"));

        verify(hotelViewTransformer).transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO);
        verify(hotelService).createHotel(HOTEL_CREATION_SERVICE_DTO);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldRejectHotelIfAnyOtherErrorHappenedDuringSavingAndReturnToAddHotelsPageWithGlobalError() throws Exception {
        when(hotelViewTransformer.transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO)).thenReturn(HOTEL_CREATION_SERVICE_DTO);
        when(hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO)).thenThrow(DataIntegrityViolationException.class); // Example error that might occur during JpaRepository.save

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(model().attributeErrorCount("hotelCreationDTO", 1))
                .andExpect(model().errorCount(1))
                .andExpect(view().name("addhotels"));

        verify(hotelViewTransformer).transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO);
        verify(hotelService).createHotel(HOTEL_CREATION_SERVICE_DTO);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testCreateNewHotelShouldAddSuccessMessageAttributeAndReturnToAddHotelsPage() throws Exception {
        when(hotelViewTransformer.transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO)).thenReturn(HOTEL_CREATION_SERVICE_DTO);
        when(hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(model().attributeErrorCount("hotelCreationDTO", 0))
                .andExpect(model().errorCount(0))
                .andExpect(view().name("addhotels"));

        verify(hotelViewTransformer).transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO);
        verify(hotelService).createHotel(HOTEL_CREATION_SERVICE_DTO);
    }
}
