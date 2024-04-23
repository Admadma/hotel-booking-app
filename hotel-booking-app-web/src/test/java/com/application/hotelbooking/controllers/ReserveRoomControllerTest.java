package com.application.hotelbooking.controllers;

import com.application.hotelbooking.dto.*;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.models.ReservationModel;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.transformers.HotelsWithReservableRoomsDTOTransformer;
import com.application.hotelbooking.transformers.ReservationPlanTransformer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfiguration.class)
@WebMvcTest(ReserveRoomController.class)
public class ReserveRoomControllerTest {

    private static final String HOTEL_NAME = "TEST_HOTEL";
    private static final int ROOM_NUMBER = 1;
    private static final LocalDate START_DATE = LocalDate.of(2024, 3, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 3, 5);
    private static final String USER_NAME = "testUser";
    private static final com.application.hotelbooking.models.RoomType SINGLE_ROOM_MODEL = com.application.hotelbooking.models.RoomType.SINGLE_ROOM;
    private static final com.application.hotelbooking.views.RoomType SINGLE_ROOM_VIEW = com.application.hotelbooking.views.RoomType.SINGLE_ROOM;

    private static final ReservationModel RESERVATION_MODEL = new ReservationModel();

    private static UniqueReservableRoomOfHotelDTO UNIQUE_RESERVABLE_ROOM_OF_HOTEL_DTO = UniqueReservableRoomOfHotelDTO.builder()
            .number(1)
            .singleBeds(1)
            .doubleBeds(0)
            .pricePerNight(10)
            .totalPrice(40)
            .roomType(SINGLE_ROOM_VIEW)
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
    private static List<HotelWithReservableRoomsServiceDTO> HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST = List.of(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO);
    private static final ReservationPlanServiceDTO RESERVATION_PLAN_SERVICE_DTO = ReservationPlanServiceDTO.builder()
            .hotelName(HOTEL_NAME)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();
    private static final ReservationPlanDTO RESERVATION_PLAN_DTO = ReservationPlanDTO.builder()
            .hotelName(HOTEL_NAME)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();

    @MockBean
    private ReservationService reservationService;
    @MockBean
    private HotelsWithReservableRoomsDTOTransformer hotelsWithReservableRoomsDTOTransformer;
    @MockBean
    private ReservationPlanTransformer reservationPlanTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = USER_NAME, authorities = "USER")
    public void testUserCanNavigateToReserveRoomPageAndGetReservationPlanOfSelectedRoomOfHotel() throws Exception {
        when(hotelsWithReservableRoomsDTOTransformer.transformToHotelWithReservableRoomsServiceDTOs(HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST)).thenReturn(HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
        when(reservationService.createReservationPlan(ROOM_NUMBER, HOTEL_NAME, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST)).thenReturn(RESERVATION_PLAN_SERVICE_DTO);
        when(reservationPlanTransformer.transformToReservationPlanDTO(RESERVATION_PLAN_SERVICE_DTO)).thenReturn(RESERVATION_PLAN_DTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/reserve-room")
                        .param("hotelName", HOTEL_NAME)
                        .param("number", String.valueOf(ROOM_NUMBER))
                        .sessionAttr("hotelsRoomsResultDTOs", HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("reservationPlan", RESERVATION_PLAN_DTO))
                .andExpect(view().name("reserveroom"));

        verify(hotelsWithReservableRoomsDTOTransformer).transformToHotelWithReservableRoomsServiceDTOs(HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST);
        verify(reservationService).createReservationPlan(ROOM_NUMBER, HOTEL_NAME, HOTEL_WITH_RESERVABLE_ROOMS_SERVICE_DTO_LIST);
        verify(reservationPlanTransformer).transformToReservationPlanDTO(RESERVATION_PLAN_SERVICE_DTO);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminUserCanNotAttemptToPrepareReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/reserve-room")
                        .param("hotelName", HOTEL_NAME)
                        .param("number", String.valueOf(ROOM_NUMBER))
                        .sessionAttr("hotelsRoomsResultDTOs", HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST))
                .andExpect(status().isForbidden())
                .andExpect(request().sessionAttribute("hotelsRoomsResultDTOs", HOTEL_WITH_RESERVABLE_ROOMS_DTO_LIST));
    }

    @Test
    @WithMockUser(authorities = "ADMIN", username = "admin")
    public void testAdminUserCanNotAttemptToReserveRoom() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/reserve-room/reserve")
                        .sessionAttr("reservationPlan", RESERVATION_PLAN_DTO))
                .andExpect(status().isForbidden())
                .andExpect(request().sessionAttribute("reservationPlan", RESERVATION_PLAN_DTO));
    }

    @Test
    @WithMockUser(authorities = "USER", username = USER_NAME)
    public void testReserveShouldRedirectToHomePageWithReservationErrorAndClearSessionAttributesIfOutdatedReservationExceptionWasThrown() throws Exception {
        when(reservationPlanTransformer.transformToReservationPlanServiceDTO(RESERVATION_PLAN_DTO)).thenReturn(RESERVATION_PLAN_SERVICE_DTO);
        when(reservationService.reserveRoom(RESERVATION_PLAN_SERVICE_DTO, USER_NAME)).thenThrow(OutdatedReservationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/reserve-room/reserve")
                        .sessionAttr("reservationPlan", RESERVATION_PLAN_DTO))
                .andExpect(redirectedUrl("/hotelbooking/home?reservationError"))
                .andExpect(request().sessionAttributeDoesNotExist("reservationPlan"))
                .andExpect(request().sessionAttributeDoesNotExist("hotelsRoomsResultDTOs"));

        verify(reservationPlanTransformer).transformToReservationPlanServiceDTO(RESERVATION_PLAN_DTO);
        verify(reservationService).reserveRoom(RESERVATION_PLAN_SERVICE_DTO, USER_NAME);
    }

    @Test
    @WithMockUser(authorities = "USER", username = USER_NAME)
    public void testReserveShouldRedirectToHomePageWithReservationErrorAndClearSessionAttributesIfAnyOtherExceptionWasThrown() throws Exception {
        when(reservationPlanTransformer.transformToReservationPlanServiceDTO(RESERVATION_PLAN_DTO)).thenReturn(RESERVATION_PLAN_SERVICE_DTO);
        when(reservationService.reserveRoom(RESERVATION_PLAN_SERVICE_DTO, USER_NAME)).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/reserve-room/reserve")
                        .sessionAttr("reservationPlan", RESERVATION_PLAN_DTO))
                .andExpect(redirectedUrl("/hotelbooking/home?reservationError"))
                .andExpect(request().sessionAttributeDoesNotExist("reservationPlan"))
                .andExpect(request().sessionAttributeDoesNotExist("resultDTOS"));

        verify(reservationPlanTransformer).transformToReservationPlanServiceDTO(RESERVATION_PLAN_DTO);
        verify(reservationService).reserveRoom(RESERVATION_PLAN_SERVICE_DTO, USER_NAME);
    }

    @Test
    @WithMockUser(authorities = "USER", username = USER_NAME)
    public void testReserveShouldRedirectToMyReservationsPageWithReservationSuccessAndClearSessionAttributesIfRoomSuccessfullyReserved() throws Exception {
        when(reservationPlanTransformer.transformToReservationPlanServiceDTO(RESERVATION_PLAN_DTO)).thenReturn(RESERVATION_PLAN_SERVICE_DTO);
        when(reservationService.reserveRoom(RESERVATION_PLAN_SERVICE_DTO, USER_NAME)).thenReturn(RESERVATION_MODEL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/reserve-room/reserve")
                        .sessionAttr("reservationPlan", RESERVATION_PLAN_DTO))
                .andExpect(redirectedUrl("/hotelbooking/my-reservations?reservationSuccess"))
                .andExpect(request().sessionAttributeDoesNotExist("reservationPlan"))
                .andExpect(request().sessionAttributeDoesNotExist("resultDTOS"));

        verify(reservationPlanTransformer).transformToReservationPlanServiceDTO(RESERVATION_PLAN_DTO);
        verify(reservationService).reserveRoom(RESERVATION_PLAN_SERVICE_DTO, USER_NAME);
    }
}
