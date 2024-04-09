package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.HotelView;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.ReservableRoomViewDTO;
import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import com.application.hotelbooking.transformers.RoomSearchDTOTransformer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Import(SecurityConfiguration.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    private static final LocalDate LOCAL_DATE_NOW_PLUS_FIVE_DAYS = LocalDate.now().plusDays(5);
    private static final LocalDate LOCAL_DATE_NOW_PLUS_TEN_DAYS = LocalDate.now().plusDays(10);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO = new RoomSearchFormDTO(1, 1, RoomType.FAMILY_ROOM, "", "", LOCAL_DATE_NOW_PLUS_FIVE_DAYS, LOCAL_DATE_NOW_PLUS_TEN_DAYS);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_NOT_EMPTY_HOTEL = new RoomSearchFormDTO(1, 1, RoomType.FAMILY_ROOM, "Test Hotel", "Test City", LOCAL_DATE_NOW_PLUS_FIVE_DAYS, LOCAL_DATE_NOW_PLUS_TEN_DAYS);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_REPLACED = new RoomSearchFormDTO(1, 1, RoomType.FAMILY_ROOM, null, null, LOCAL_DATE_NOW_PLUS_FIVE_DAYS, LOCAL_DATE_NOW_PLUS_TEN_DAYS);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_WITH_FOUR_INVALID_FIELDS = new RoomSearchFormDTO(-1, -1, RoomType.FAMILY_ROOM, "", "", null, null);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_START_DATE_AFTER_END_DATE = new RoomSearchFormDTO(1, 1, RoomType.FAMILY_ROOM, "Test Hotel", "Test City", LOCAL_DATE_NOW_PLUS_TEN_DAYS, LOCAL_DATE_NOW_PLUS_FIVE_DAYS);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_START_END_DATE_SAME = new RoomSearchFormDTO(1, 1, RoomType.FAMILY_ROOM, "Test Hotel", "Test City", LOCAL_DATE_NOW_PLUS_FIVE_DAYS, LOCAL_DATE_NOW_PLUS_FIVE_DAYS);
    private static final RoomSearchFormServiceDTO ROOM_SEARCH_FORM_SERVICE_DTO = RoomSearchFormServiceDTO.builder()
            .singleBeds(ROOM_SEARCH_FORM_DTO_REPLACED.getSingleBeds())
            .doubleBeds(ROOM_SEARCH_FORM_DTO_REPLACED.getDoubleBeds())
            .hotelName(ROOM_SEARCH_FORM_DTO_REPLACED.getHotelName())
            .city(ROOM_SEARCH_FORM_DTO_REPLACED.getCity())
            .roomType(ROOM_SEARCH_FORM_DTO_REPLACED.getRoomType())
            .startDate(ROOM_SEARCH_FORM_DTO_REPLACED.getStartDate())
            .endDate(ROOM_SEARCH_FORM_DTO_REPLACED.getEndDate())
            .build();
    private static final ReservableRoomDTO RESERVABLE_ROOM_DTO = ReservableRoomDTO.builder()
            .roomNumber(1)
            .totalPrice(100)
            .singleBeds(ROOM_SEARCH_FORM_DTO_REPLACED.getSingleBeds())
            .doubleBeds(ROOM_SEARCH_FORM_DTO_REPLACED.getDoubleBeds())
            .hotelName(ROOM_SEARCH_FORM_DTO_REPLACED.getHotelName())
            .city(ROOM_SEARCH_FORM_DTO_REPLACED.getCity())
            .roomType(ROOM_SEARCH_FORM_DTO_REPLACED.getRoomType())
            .startDate(ROOM_SEARCH_FORM_DTO_REPLACED.getStartDate())
            .endDate(ROOM_SEARCH_FORM_DTO_REPLACED.getEndDate())
            .build();
    private static final List<ReservableRoomDTO> RESERVABLE_ROOM_DTO_LIST = List.of(RESERVABLE_ROOM_DTO);
    private static final ReservableRoomViewDTO RESERVABLE_ROOM_VIEW_DTO = new ReservableRoomViewDTO();
    private static final List<ReservableRoomViewDTO> RESERVABLE_ROOM_VIEW_DTO_LIST = List.of(RESERVABLE_ROOM_VIEW_DTO);
    private static final HotelModel HOTEL_MODEL = HotelModel.builder().hotelName("Test Hotel").city("Test City").build();
    private static final HotelView HOTEL_VIEW = HotelView.builder().hotelName("Test Hotel").city("Test City").build();
    private static final List<HotelModel> HOTEL_MODEL_LIST = List.of(HOTEL_MODEL);
    private static final List<HotelView> HOTEL_VIEW_LIST = List.of(HOTEL_VIEW);
    private static final List<String> LIST_OF_ROOM_TYPES = Arrays.stream(RoomType.values()).map(roomType -> roomType.name()).collect(Collectors.toList());
    private static final List<String> LIST_OF_CITIES = List.of("Test City");

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
                .andExpect(request().sessionAttribute("roomTypes", LIST_OF_ROOM_TYPES))
                .andExpect(request().sessionAttribute("hotels", HOTEL_VIEW_LIST))
                .andExpect(request().sessionAttribute("cities", LIST_OF_CITIES));

        verify(hotelRepositoryService, times(2)).getAllHotels();
        verify(hotelViewTransformer).transformToHotelViews(HOTEL_MODEL_LIST);
    }

    @Test
    public void testSearchRoomsReturnsToHomePageIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO_WITH_FOUR_INVALID_FIELDS))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeExists("roomSearchFormDTO"))
                .andExpect(model().attributeErrorCount("roomSearchFormDTO", 4))
                .andExpect(model().attribute("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO_WITH_FOUR_INVALID_FIELDS))
                .andExpect(model().attributeHasErrors("roomSearchFormDTO"));
    }

    @Test
    public void testSearchRoomsReturnsToHomePageAndRejectsDatesIfStartDateIsAfterEndDate() throws Exception {
        when(roomService.isEndDateAfterStartDate(LOCAL_DATE_NOW_PLUS_TEN_DAYS, LOCAL_DATE_NOW_PLUS_FIVE_DAYS)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO_START_DATE_AFTER_END_DATE))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeHasFieldErrorCode("roomSearchFormDTO", "startDate", "home.room.form.validation.startdate.must.before"))
                .andExpect(model().attributeHasFieldErrorCode("roomSearchFormDTO", "endDate", "home.room.form.validation.enddate.must.after"));

        verify(roomService).isEndDateAfterStartDate(LOCAL_DATE_NOW_PLUS_TEN_DAYS, LOCAL_DATE_NOW_PLUS_FIVE_DAYS);
    }

    @Test
    public void testSearchRoomsReturnsToHomePageAndRejectsDatesIfStartDateAndEndDateAreTheSame() throws Exception {
        when(roomService.isEndDateAfterStartDate(LOCAL_DATE_NOW_PLUS_FIVE_DAYS, LOCAL_DATE_NOW_PLUS_FIVE_DAYS)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO_START_END_DATE_SAME))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeHasFieldErrorCode("roomSearchFormDTO", "startDate", "home.room.form.validation.startdate.must.before"))
                .andExpect(model().attributeHasFieldErrorCode("roomSearchFormDTO", "endDate", "home.room.form.validation.enddate.must.after"));

        verify(roomService).isEndDateAfterStartDate(LOCAL_DATE_NOW_PLUS_FIVE_DAYS, LOCAL_DATE_NOW_PLUS_FIVE_DAYS);
    }

    @Test
    public void testSearchRoomFetchesReservableRoomsAndAddsThemToSessionAndRedirectsToHomePageWithTheSuccessfulFormDTO() throws Exception {
        when(roomService.isEndDateAfterStartDate(ROOM_SEARCH_FORM_DTO.getStartDate(), ROOM_SEARCH_FORM_DTO.getEndDate())).thenReturn(true);
        when(roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_REPLACED)).thenReturn(ROOM_SEARCH_FORM_SERVICE_DTO);
        when(roomService.searchRooms(ROOM_SEARCH_FORM_SERVICE_DTO)).thenReturn(RESERVABLE_ROOM_DTO_LIST);
        when(roomSearchDTOTransformer.transformToRoomSearchResultViewDTOs(RESERVABLE_ROOM_DTO_LIST)).thenReturn(RESERVABLE_ROOM_VIEW_DTO_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO))
                .andExpect(flash().attribute("successRoomSearchFormDTO", ROOM_SEARCH_FORM_DTO))
                .andExpect(request().sessionAttribute("resultDTOS", RESERVABLE_ROOM_VIEW_DTO_LIST))
                .andExpect(redirectedUrl("/hotelbooking/home"));

        verify(roomService).isEndDateAfterStartDate(ROOM_SEARCH_FORM_DTO.getStartDate(), ROOM_SEARCH_FORM_DTO.getEndDate());
        verify(roomSearchDTOTransformer).transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_REPLACED);
        verify(roomService).searchRooms(ROOM_SEARCH_FORM_SERVICE_DTO);
        verify(roomSearchDTOTransformer).transformToRoomSearchResultViewDTOs(RESERVABLE_ROOM_DTO_LIST);
    }

    @Test
    public void testSearchRoomFetchesReservableRoomsAndAddsThemToSessionAndRedirectsToHomePageWithTheSuccessfulFormDTOWithoutReplacingEmptyStrings() throws Exception {
        when(roomService.isEndDateAfterStartDate(ROOM_SEARCH_FORM_DTO.getStartDate(), ROOM_SEARCH_FORM_DTO.getEndDate())).thenReturn(true);
        when(roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_NOT_EMPTY_HOTEL)).thenReturn(ROOM_SEARCH_FORM_SERVICE_DTO);
        when(roomService.searchRooms(ROOM_SEARCH_FORM_SERVICE_DTO)).thenReturn(RESERVABLE_ROOM_DTO_LIST);
        when(roomSearchDTOTransformer.transformToRoomSearchResultViewDTOs(RESERVABLE_ROOM_DTO_LIST)).thenReturn(RESERVABLE_ROOM_VIEW_DTO_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO_NOT_EMPTY_HOTEL))
                .andExpect(flash().attribute("successRoomSearchFormDTO", ROOM_SEARCH_FORM_DTO_NOT_EMPTY_HOTEL))
                .andExpect(request().sessionAttribute("resultDTOS", RESERVABLE_ROOM_VIEW_DTO_LIST))
                .andExpect(redirectedUrl("/hotelbooking/home"));

        verify(roomService).isEndDateAfterStartDate(ROOM_SEARCH_FORM_DTO.getStartDate(), ROOM_SEARCH_FORM_DTO.getEndDate());
        verify(roomSearchDTOTransformer).transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_NOT_EMPTY_HOTEL);
        verify(roomService).searchRooms(ROOM_SEARCH_FORM_SERVICE_DTO);
        verify(roomSearchDTOTransformer).transformToRoomSearchResultViewDTOs(RESERVABLE_ROOM_DTO_LIST);
    }
}
