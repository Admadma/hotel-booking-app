package com.application.hotelbooking.controllers;

import com.application.hotelbooking.models.HotelModel;
import com.application.hotelbooking.domain.HotelView;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.RoomService;
import com.application.hotelbooking.services.repositoryservices.HotelRepositoryService;
import com.application.hotelbooking.transformers.HotelViewTransformer;
import com.application.hotelbooking.transformers.HotelsWithReservableRoomsDTOTransformer;
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

    private static final com.application.hotelbooking.models.RoomType FAMILY_ROOM_MODEL = com.application.hotelbooking.models.RoomType.FAMILY_ROOM;
    private static final com.application.hotelbooking.models.RoomType SINGLE_ROOM_MODEL = com.application.hotelbooking.models.RoomType.SINGLE_ROOM;
    private static final com.application.hotelbooking.domain.RoomType FAMILY_ROOM_DOMAIN = com.application.hotelbooking.domain.RoomType.FAMILY_ROOM;
    private static final LocalDate START_DATE = LocalDate.now().plusDays(7);
    private static final LocalDate END_DATE = LocalDate.now().plusDays(14);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO = new RoomSearchFormDTO(1, 1, FAMILY_ROOM_DOMAIN, "", "", START_DATE, END_DATE);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL = new RoomSearchFormDTO(1, 1, FAMILY_ROOM_DOMAIN, null, null, START_DATE, END_DATE);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_WITH_FOUR_INVALID_FIELDS = new RoomSearchFormDTO(-1, -1, FAMILY_ROOM_DOMAIN, "", "", null, null);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_START_DATE_AFTER_END_DATE = new RoomSearchFormDTO(1, 1, FAMILY_ROOM_DOMAIN, "Test Hotel", "Test City", END_DATE, START_DATE);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_START_END_DATE_SAME = new RoomSearchFormDTO(1, 1, FAMILY_ROOM_DOMAIN, "Test Hotel", "Test City", START_DATE, START_DATE);
    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO_ONLY_START_END_DATE = RoomSearchFormDTO.builder().startDate(START_DATE).endDate(END_DATE).build();
    private static final RoomSearchFormServiceDTO ROOM_SEARCH_FORM_SERVICE_DTO_ONLY_START_END_DATE = RoomSearchFormServiceDTO.builder().startDate(START_DATE).endDate(END_DATE).build();
    private static final RoomSearchFormServiceDTO ROOM_SEARCH_FORM_SERVICE_DTO = RoomSearchFormServiceDTO.builder()
            .singleBeds(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL.getSingleBeds())
            .doubleBeds(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL.getDoubleBeds())
            .hotelName(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL.getHotelName())
            .city(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL.getCity())
            .roomType(FAMILY_ROOM_MODEL)
            .startDate(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL.getStartDate())
            .endDate(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL.getEndDate())
            .build();
    private static final HotelModel HOTEL_MODEL = HotelModel.builder().hotelName("Test Hotel").city("Test City").build();
    private static final HotelView HOTEL_VIEW = HotelView.builder().hotelName("Test Hotel").city("Test City").build();
    private static final List<HotelModel> HOTEL_MODEL_LIST = List.of(HOTEL_MODEL);
    private static final List<HotelView> HOTEL_VIEW_LIST = List.of(HOTEL_VIEW);
    private static final List<String> LIST_OF_ROOM_TYPES = Arrays.stream(RoomType.values()).map(roomType -> roomType.name()).collect(Collectors.toList());
    private static final List<String> LIST_OF_CITIES = List.of("Test City");
    private static UniqueReservableRoomOfHotelDTO UNIQUE_RESERVABLE_ROOM_OF_HOTEL_DTO = UniqueReservableRoomOfHotelDTO.builder()
            .number(1)
            .singleBeds(1)
            .doubleBeds(0)
            .pricePerNight(10)
            .totalPrice(40)
            .roomType(FAMILY_ROOM_DOMAIN)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();
    private static UniqueReservableRoomOfHotelServiceDTO UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO = UniqueReservableRoomOfHotelServiceDTO.builder()
            .number(1)
            .singleBeds(1)
            .doubleBeds(0)
            .pricePerNight(10)
            .totalPrice(40)
            .roomType(SINGLE_ROOM_MODEL)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();
    private static HotelWithReservableRoomsDTO HOTEL_WITH_RESERVABLE_ROOMS_DTO = HotelWithReservableRoomsDTO.builder()
            .uniqueReservableRoomOfHotelDTOList(List.of(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_DTO))
            .build();
    private static HotelWithReservableRoomsServiceDTO HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO = HotelWithReservableRoomsServiceDTO.builder()
            .uniqueReservableRoomOfHotelServiceDTOList(List.of(UNIQUE_RESERVABLE_ROOM_OF_HOTEL_SERVICE_DTO))
            .build();
    private static List<HotelWithReservableRoomsDTO> HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST = List.of(HOTEL_WITH_RESERVABLE_ROOMS_DTO);
    private static List<HotelWithReservableRoomsDTO> EMPTY_HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST = List.of();
    private static List<HotelWithReservableRoomsServiceDTO> HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST = List.of(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO);
    private static List<HotelWithReservableRoomsServiceDTO> EMPTY_HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST = List.of();

    @MockBean
    private RoomService roomService;

    @MockBean
    private HotelRepositoryService hotelRepositoryService;

    @MockBean
    private RoomSearchDTOTransformer roomSearchDTOTransformer;

    @MockBean
    private HotelViewTransformer hotelViewTransformer;

    @MockBean
    private HotelsWithReservableRoomsDTOTransformer hotelsWithReservableRoomsDTOTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testNavigatingToHomePageAddsEmptyRoomSearchFormDTOToModelAttributesAndSetsSessionAttributesAndSetsHotelsRoomsResultDTOsSessionAttributeIfItsNotAlreadyPresent() throws Exception {
        when(hotelRepositoryService.getAllHotels()).thenReturn(HOTEL_MODEL_LIST);
        when(hotelViewTransformer.transformToHotelViews(HOTEL_MODEL_LIST)).thenReturn(HOTEL_VIEW_LIST);
        when(roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_ONLY_START_END_DATE)).thenReturn(ROOM_SEARCH_FORM_SERVICE_DTO_ONLY_START_END_DATE);
        when(roomService.searchHotelsWithReservableRooms(ROOM_SEARCH_FORM_SERVICE_DTO_ONLY_START_END_DATE)).thenReturn(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
        when(hotelsWithReservableRoomsDTOTransformer.transformToHotelsWithReservableRoomsDTOs(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST)).thenReturn(HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeExists("roomSearchFormDTO"))
                .andExpect(model().attribute("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO_ONLY_START_END_DATE))
                .andExpect(request().sessionAttribute("roomTypes", LIST_OF_ROOM_TYPES))
                .andExpect(request().sessionAttribute("hotels", HOTEL_VIEW_LIST))
                .andExpect(request().sessionAttribute("cities", LIST_OF_CITIES))
                .andExpect(request().sessionAttribute("hotelsRoomsResultDTOs", HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST));

        verify(hotelRepositoryService, times(2)).getAllHotels();
        verify(hotelViewTransformer).transformToHotelViews(HOTEL_MODEL_LIST);
        verify(roomSearchDTOTransformer).transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_ONLY_START_END_DATE);
        verify(roomService).searchHotelsWithReservableRooms(ROOM_SEARCH_FORM_SERVICE_DTO_ONLY_START_END_DATE);
        verify(hotelsWithReservableRoomsDTOTransformer).transformToHotelsWithReservableRoomsDTOs(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
    }

    @Test
    public void testNavigatingToHomePageWithRoomSearchFormSubmittedAndHotelsRoomsResultDTOsPresentAddsTheReceivedModelAttributeAndSetsTheSessionAttributesAndDoesNotSetNewHotelsRoomsResultDTOs() throws Exception {
        when(hotelRepositoryService.getAllHotels()).thenReturn(HOTEL_MODEL_LIST);
        when(hotelViewTransformer.transformToHotelViews(HOTEL_MODEL_LIST)).thenReturn(HOTEL_VIEW_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/home")
                        .flashAttr("successRoomSearchFormDTO", ROOM_SEARCH_FORM_DTO)
                        .sessionAttr("hotelsRoomsResultDTOs", HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeExists("roomSearchFormDTO"))
                .andExpect(model().attribute("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO))
                .andExpect(request().sessionAttribute("roomTypes", LIST_OF_ROOM_TYPES))
                .andExpect(request().sessionAttribute("hotels", HOTEL_VIEW_LIST))
                .andExpect(request().sessionAttribute("cities", LIST_OF_CITIES))
                .andExpect(request().sessionAttribute("hotelsRoomsResultDTOs", HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST));

        verify(hotelRepositoryService, times(2)).getAllHotels();
        verify(hotelViewTransformer).transformToHotelViews(HOTEL_MODEL_LIST);
    }

    @Test
    public void testSearchRoomsReturnsToHomePageIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/home/search-rooms")
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
        when(roomService.isEndDateAfterStartDate(END_DATE, START_DATE)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/home/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO_START_DATE_AFTER_END_DATE))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeHasFieldErrorCode("roomSearchFormDTO", "startDate", "home.room.form.validation.startdate.must.before"))
                .andExpect(model().attributeHasFieldErrorCode("roomSearchFormDTO", "endDate", "home.room.form.validation.enddate.must.after"));

        verify(roomService).isEndDateAfterStartDate(END_DATE, START_DATE);
    }

    @Test
    public void testSearchRoomsReturnsToHomePageAndRejectsDatesIfStartDateAndEndDateAreTheSame() throws Exception {
        when(roomService.isEndDateAfterStartDate(START_DATE, START_DATE)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/home/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO_START_END_DATE_SAME))
                .andExpect(status().isOk())
                .andExpect(view().name("homepage"))
                .andExpect(model().attributeHasFieldErrorCode("roomSearchFormDTO", "startDate", "home.room.form.validation.startdate.must.before"))
                .andExpect(model().attributeHasFieldErrorCode("roomSearchFormDTO", "endDate", "home.room.form.validation.enddate.must.after"));

        verify(roomService).isEndDateAfterStartDate(START_DATE, START_DATE);
    }

    @Test
    public void testSearchRoomFetchesHotelsWithAvailableRoomsAndSetsThemInSessionAttributeIfPresent() throws Exception {
        when(roomService.isEndDateAfterStartDate(ROOM_SEARCH_FORM_DTO.getStartDate(), ROOM_SEARCH_FORM_DTO.getEndDate())).thenReturn(true);
        when(roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL)).thenReturn(ROOM_SEARCH_FORM_SERVICE_DTO);
        when(roomService.searchHotelsWithReservableRooms(ROOM_SEARCH_FORM_SERVICE_DTO)).thenReturn(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
        when(hotelsWithReservableRoomsDTOTransformer.transformToHotelsWithReservableRoomsDTOs(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST)).thenReturn(HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/home/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO))
                .andExpect(request().sessionAttribute("hotelsRoomsResultDTOs", HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST))
                .andExpect(request().sessionAttributeDoesNotExist("noMatchingResults"))
                .andExpect(redirectedUrl("/hotelbooking/home"));

        verify(roomService).isEndDateAfterStartDate(ROOM_SEARCH_FORM_DTO.getStartDate(), ROOM_SEARCH_FORM_DTO.getEndDate());
        verify(roomSearchDTOTransformer).transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL);
        verify(roomService).searchHotelsWithReservableRooms(ROOM_SEARCH_FORM_SERVICE_DTO);
        verify(hotelsWithReservableRoomsDTOTransformer).transformToHotelsWithReservableRoomsDTOs(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
    }

    @Test
    public void testSearchRoomFetchesHotelsWithAvailableRoomsAndSetsNoMatchingResultsIfNoRoomMatchesTheSearchCondition() throws Exception {
        when(roomService.isEndDateAfterStartDate(ROOM_SEARCH_FORM_DTO.getStartDate(), ROOM_SEARCH_FORM_DTO.getEndDate())).thenReturn(true);
        when(roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL)).thenReturn(ROOM_SEARCH_FORM_SERVICE_DTO);
        when(roomService.searchHotelsWithReservableRooms(ROOM_SEARCH_FORM_SERVICE_DTO)).thenReturn(EMPTY_HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
        when(hotelsWithReservableRoomsDTOTransformer.transformToHotelsWithReservableRoomsDTOs(EMPTY_HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST)).thenReturn(EMPTY_HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/home/search-rooms")
                        .flashAttr("roomSearchFormDTO", ROOM_SEARCH_FORM_DTO))
                .andExpect(request().sessionAttribute("noMatchingResults", true))
                .andExpect(request().sessionAttributeDoesNotExist("hotelsRoomsResultDTOs"))
                .andExpect(redirectedUrl("/hotelbooking/home"));

        verify(roomService).isEndDateAfterStartDate(ROOM_SEARCH_FORM_DTO.getStartDate(), ROOM_SEARCH_FORM_DTO.getEndDate());
        verify(roomSearchDTOTransformer).transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO_REPLACED_EMPTY_STRINGS_WITH_NULL);
        verify(roomService).searchHotelsWithReservableRooms(ROOM_SEARCH_FORM_SERVICE_DTO);
        verify(hotelsWithReservableRoomsDTOTransformer).transformToHotelsWithReservableRoomsDTOs(EMPTY_HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
    }
}
