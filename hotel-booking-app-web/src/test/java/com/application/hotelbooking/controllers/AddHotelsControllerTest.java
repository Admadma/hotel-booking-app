package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.HotelCreationDTO;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AddHotelsController.class)
public class AddHotelsControllerTest {

    @MockBean
    private HotelService hotelService;

    @MockBean
    private HotelViewTransformer hotelViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER", "ADMIN"})
    public void testAdminUserCanNavigateToAddHotelsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/addHotels"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("addhotels"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("hotelCreationDTO"))
                .andExpect(MockMvcResultMatchers.model().attribute("hotelCreationDTO", Matchers.any(HotelCreationDTO.class)));
    }
}
