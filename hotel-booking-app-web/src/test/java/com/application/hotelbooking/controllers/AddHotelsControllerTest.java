package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.HotelCreationDTO;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.services.HotelService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddHotelsController.class)
public class AddHotelsControllerTest {

    private HotelCreationDTO HOTEL_CREATION_DTO = new HotelCreationDTO("Test Hotel", "Test City");
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
    @WithMockUser(username = "testUser", roles = "ADMIN")
    public void testAdminUserCanNavigateToAddHotelsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/addHotels"))
                .andExpect(status().isOk())
                .andExpect(view().name("addhotels"))
                .andExpect(model().attributeExists("hotelCreationDTO"))
                .andExpect(model().attribute("hotelCreationDTO", Matchers.any(HotelCreationDTO.class)));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    public void testCreateNewHotelShouldReturnToAddHotelsPageWithErrorIfProvidedAttributesAreInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_SHORT_NAME_HAS_CITY)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(view().name("addhotels"))
                .andExpect(model().attribute("hotelCreationDTO", Matchers.any(HotelCreationDTO.class)))
                .andExpect(model().attributeHasErrors("hotelCreationDTO"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_GOOD_NAME_NO_CITY)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(view().name("addhotels"))
                .andExpect(model().attribute("hotelCreationDTO", Matchers.any(HotelCreationDTO.class)))
                .andExpect(model().attributeHasErrors("hotelCreationDTO"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    public void testCreateNewHotelShouldRejectHotelNameAndReturnToAddHotelsPageWithErrorCode() throws Exception {
        when(hotelViewTransformer.transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO)).thenReturn(HOTEL_CREATION_SERVICE_DTO);
        when(hotelService.createHotel(HOTEL_CREATION_SERVICE_DTO)).thenThrow(InvalidHotelException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/create-new-hotel")
                        .flashAttr("hotelCreationDTO", HOTEL_CREATION_DTO)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attribute("hotelCreationDTO", Matchers.any(HotelCreationDTO.class)))
                .andExpect(model().attribute("hotelCreationDTO", HOTEL_CREATION_DTO))
                .andExpect(model().attributeHasFieldErrorCode("hotelCreationDTO", "hotelName", "admin.hotel.validation.hotelname.taken"))
                .andExpect(view().name("addhotels"));

        verify(hotelViewTransformer).transformToHotelCreationServiceDTO(HOTEL_CREATION_DTO);
        verify(hotelService).createHotel(HOTEL_CREATION_SERVICE_DTO);
    }
}
