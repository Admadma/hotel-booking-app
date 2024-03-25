package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.ReservableRoomViewDTO;
import com.application.hotelbooking.exceptions.InvalidHotelException;
import com.application.hotelbooking.exceptions.OutdatedReservationException;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.imagehandling.FileSystemStorageService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import com.application.hotelbooking.transformers.RoomSearchDTOTransformer;
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

@Import({SecurityConfiguration.class, FileSystemStorageService.class})
@WebMvcTest(ReserveRoomController.class)
public class ReserveRoomControllerTest {

    private static final ReservableRoomViewDTO RESERVABLE_ROOM_VIEW_DTO = new ReservableRoomViewDTO();
    private static final ReservableRoomDTO RESERVABLE_ROOM_DTO = new ReservableRoomDTO();
    private static final List<ReservableRoomViewDTO> RESERVABLE_ROOM_VIEW_DTO_LIST = List.of(RESERVABLE_ROOM_VIEW_DTO);
    private static final HotelView HOTEL_VIEW = HotelView.builder()
            .hotelName("Test Hotel")
            .city("Test City")
            .build();
    private static final RoomView ROOM_VIEW = RoomView.builder()
            .roomNumber(1)
            .hotel(HOTEL_VIEW)
            .roomType(RoomType.FAMILY_ROOM)
            .singleBeds(1)
            .doubleBeds(1)
            .build();
    private static final ReservationView RESERVATION_VIEW = ReservationView.builder()
            .room(ROOM_VIEW)
            .startDate(LocalDate.now().plusDays(5))
            .endDate(LocalDate.now().plusDays(10))
            .totalPrice(100)
            .build();
    private static final String TEST_USER_NAME = "testUser";
    private static final ReservationModel RESERVATION_MODEL = new ReservationModel();
    @MockBean
    private ReservationService reservationService;

    @MockBean
    private RoomSearchDTOTransformer roomSearchDTOTransformer;

    @MockBean
    private ReservationViewTransformer reservationViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "USER")
    public void testUserCanNavigateToReserveRoomPageWithTheSelectedRoom() throws Exception {
        when(roomSearchDTOTransformer.transformToRoomSearchResultDTO(RESERVABLE_ROOM_VIEW_DTO)).thenReturn(RESERVABLE_ROOM_DTO);
        when(reservationService.prepareReservation(RESERVABLE_ROOM_DTO, TEST_USER_NAME)).thenReturn(RESERVATION_MODEL);
        when(reservationViewTransformer.transformToReservationView(RESERVATION_MODEL)).thenReturn(RESERVATION_VIEW);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/reserveroom")
                        .param("index", "0")
                        .sessionAttr("resultDTOS", RESERVABLE_ROOM_VIEW_DTO_LIST))
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute("reservationPlan", RESERVATION_VIEW))
                .andExpect(view().name("reserveroom"));

        verify(roomSearchDTOTransformer).transformToRoomSearchResultDTO(RESERVABLE_ROOM_VIEW_DTO);
        verify(reservationService).prepareReservation(RESERVABLE_ROOM_DTO, TEST_USER_NAME);
        verify(reservationViewTransformer).transformToReservationView(RESERVATION_MODEL);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminUserCanNotAttemptToPrepareReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/reserveroom")
                        .param("index", "0")
                        .sessionAttr("resultDTOS", RESERVABLE_ROOM_VIEW_DTO_LIST))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminUserCanNotAttemptToReserveRoom() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/reserve")
                        .sessionAttr("reservationPlan", RESERVATION_VIEW))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testReserveShouldRedirectToHomePageWithReservationErrorAndClearSessionAttributesIfOutdatedReservationExceptionWasThrown() throws Exception {
        when(reservationViewTransformer.transformToReservationModel(RESERVATION_VIEW)).thenReturn(RESERVATION_MODEL);
        when(reservationService.reserveRoom(RESERVATION_MODEL)).thenThrow(OutdatedReservationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/reserve")
                        .sessionAttr("reservationPlan", RESERVATION_VIEW))
                .andExpect(redirectedUrl("/hotelbooking/home?reservationError"))
                .andExpect(request().sessionAttributeDoesNotExist("reservationPlan"))
                .andExpect(request().sessionAttributeDoesNotExist("resultDTOS"));

        verify(reservationViewTransformer).transformToReservationModel(RESERVATION_VIEW);
        verify(reservationService).reserveRoom(RESERVATION_MODEL);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testReserveShouldRedirectToHomePageWithReservationErrorAndClearSessionAttributesIfAnyOtherExceptionWasThrown() throws Exception {
        when(reservationViewTransformer.transformToReservationModel(RESERVATION_VIEW)).thenReturn(RESERVATION_MODEL);
        when(reservationService.reserveRoom(RESERVATION_MODEL)).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/reserve")
                        .sessionAttr("reservationPlan", RESERVATION_VIEW))
                .andExpect(redirectedUrl("/hotelbooking/home?reservationError"))
                .andExpect(request().sessionAttributeDoesNotExist("reservationPlan"))
                .andExpect(request().sessionAttributeDoesNotExist("resultDTOS"));

        verify(reservationViewTransformer).transformToReservationModel(RESERVATION_VIEW);
        verify(reservationService).reserveRoom(RESERVATION_MODEL);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testReserveShouldRedirectToMyReservationsPageWithReservationSuccessAndClearSessionAttributesIfRoomSuccessfullyReserved() throws Exception {
        when(reservationViewTransformer.transformToReservationModel(RESERVATION_VIEW)).thenReturn(RESERVATION_MODEL);
        when(reservationService.reserveRoom(RESERVATION_MODEL)).thenReturn(RESERVATION_MODEL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/reserve")
                        .sessionAttr("reservationPlan", RESERVATION_VIEW))
                .andExpect(redirectedUrl("/hotelbooking/myreservations?reservationSuccess"))
                .andExpect(request().sessionAttributeDoesNotExist("reservationPlan"))
                .andExpect(request().sessionAttributeDoesNotExist("resultDTOS"));

        verify(reservationViewTransformer).transformToReservationModel(RESERVATION_VIEW);
        verify(reservationService).reserveRoom(RESERVATION_MODEL);
    }
}
