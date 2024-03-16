package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.RoomCreationDTO;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.RoomCreationService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import com.application.hotelbooking.transformers.RoomCreationDTOTransformer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfiguration.class)
@WebMvcTest(AddRoomsController.class)
public class AddRoomsControllerTest {

    @MockBean
    private RoomCreationService roomService;

    @MockBean
    private HotelRepositoryService hotelRepositoryService;

    @MockBean
    private RoomCreationDTOTransformer roomCreationDTOTransformer;

    @MockBean
    private HotelViewTransformer hotelViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminUserCanNavigateToAddHotelsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/addRooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("addrooms"))
                .andExpect(model().attributeExists("roomCreationDTO"))
                .andExpect(model().attribute("roomCreationDTO", Matchers.any(RoomCreationDTO.class)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testNonAdminUserForbiddenToNavigateToAddHotelsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/admin/addHotels"))
                .andExpect(status().isForbidden());
    }
}
