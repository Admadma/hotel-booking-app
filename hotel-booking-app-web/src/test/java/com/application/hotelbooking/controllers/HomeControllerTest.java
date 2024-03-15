package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.HotelView;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import com.application.hotelbooking.transformers.RoomSearchDTOTransformer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    private static RoomSearchFormDTO ROOM_SEARCH_FORM_DTO = new RoomSearchFormDTO(1, 1, RoomType.FAMILY_ROOM, "Test Hotel", "Test City", LocalDate.now().plusDays(5), LocalDate.now().plusDays(10));
    private static HotelModel HOTEL_MODEL = HotelModel.builder().hotelName("Test Hotel").city("Test City").build();
    private static HotelView HOTEL_VIEW = HotelView.builder().hotelName("Test Hotel").city("Test City").build();
    private static List<HotelModel> HOTEL_MODEL_LIST = List.of(HOTEL_MODEL);
    private static List<HotelView> HOTEL_VIEW_LIST = List.of(HOTEL_VIEW);
    private static List<String> LIST_OF_ROOM_TYPES = Arrays.stream(RoomType.values()).map(roomType -> roomType.name()).collect(Collectors.toList());
    private static List<String> LIST_OF_CITIES = List.of("Test City");
    @MockBean
    private RoomService roomService;

    @MockBean
    private HotelRepositoryService hotelRepositoryService;

    @MockBean
    private RoomSearchDTOTransformer roomSearchDTOTransformer;

    @MockBean
    private HotelViewTransformer hotelViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testNavigatingToHomePageWithNoRoomSearchFormSubmittedAddsEmptyFormToModelAttributesAndSetsSessionAttributes() throws Exception {
        when(hotelRepositoryService.getAllHotels()).thenReturn(HOTEL_MODEL_LIST);
        when(hotelViewTransformer.transformToHotelViews(HOTEL_MODEL_LIST)).thenReturn(HOTEL_VIEW_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeExists("roomSearchFormDTO"))
                .andExpect(model().attribute("roomSearchFormDTO", Matchers.any(RoomSearchFormDTO.class)))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("roomTypes", LIST_OF_ROOM_TYPES))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("hotels", HOTEL_VIEW_LIST))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("cities", LIST_OF_CITIES));

        verify(hotelRepositoryService, times(2)).getAllHotels();
        verify(hotelViewTransformer).transformToHotelViews(HOTEL_MODEL_LIST);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testNavigatingToHomePageWithRoomSearchFormSubmittedAddsEmptyFormToModelAttributesAndSetsSessionAttributes() throws Exception {
        when(hotelRepositoryService.getAllHotels()).thenReturn(HOTEL_MODEL_LIST);
        when(hotelViewTransformer.transformToHotelViews(HOTEL_MODEL_LIST)).thenReturn(HOTEL_VIEW_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/home")
                        .flashAttr("successRoomSearchFormDTO", ROOM_SEARCH_FORM_DTO))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeExists("roomSearchFormDTO"))
                .andExpect(model().attribute("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("roomTypes", LIST_OF_ROOM_TYPES))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("hotels", HOTEL_VIEW_LIST))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("cities", LIST_OF_CITIES));

        verify(hotelRepositoryService, times(2)).getAllHotels();
        verify(hotelViewTransformer).transformToHotelViews(HOTEL_MODEL_LIST);
    }
}
