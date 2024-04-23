package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.exceptions.InvalidReservationException;
import com.application.hotelbooking.exceptions.InvalidTokenException;
import com.application.hotelbooking.exceptions.InvalidUserException;
import com.application.hotelbooking.models.ReservationModel;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
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
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Import(SecurityConfiguration.class)
@WebMvcTest(MyReservationsController.class)
public class MyReservationsControllerTest {

    private static final Long RESERVATION_ID = 1L;
    private static final String TEST_USER_NAME = "test_user";
    private static final com.application.hotelbooking.domain.RoomType FAMILY_ROOM_DOMAIN = com.application.hotelbooking.domain.RoomType.FAMILY_ROOM;
    private static final com.application.hotelbooking.domain.ReservationStatus PLANNED_RESERVATION_STATUS_DOMAIN = com.application.hotelbooking.domain.ReservationStatus.PLANNED;
    private static final UUID TEST_UUID = UUID.fromString("2a167ea9-850c-4059-8163-6f941561c419");
    private static final LocalDate START_DATE = LocalDate.of(2024, 3, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 3, 2);
    private static final ReservationModel RESERVATION_MODEL = ReservationModel.builder().reservationStatus(com.application.hotelbooking.models.ReservationStatus.PLANNED).build();
    private static final List<ReservationModel> RESERVATION_MODEL_LIST = List.of(RESERVATION_MODEL);
    private static final HotelView HOTEL_VIEW = HotelView.builder()
            .hotelName("Test Hotel")
            .city("Test City")
            .build();
    private static final RoomView ROOM_VIEW = RoomView.builder()
            .roomNumber(1)
            .hotel(HOTEL_VIEW)
            .roomType(FAMILY_ROOM_DOMAIN)
            .build();
    private static final ReservationView RESERVATION_VIEW = ReservationView.builder()
            .room(ROOM_VIEW)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .totalPrice(100)
            .reservationStatus(PLANNED_RESERVATION_STATUS_DOMAIN)
            .build();
    private static final List<ReservationView> RESERVATION_VIEW_LIST = List.of(RESERVATION_VIEW);

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private ReservationViewTransformer reservationViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "USER")
    public void testUserCanNavigateToMyReservationsPage() throws Exception {
        when(reservationService.getReservationsOfUser(TEST_USER_NAME)).thenReturn(RESERVATION_MODEL_LIST);
        when(reservationViewTransformer.transformToReservationViews(RESERVATION_MODEL_LIST)).thenReturn(RESERVATION_VIEW_LIST);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/my-reservations"))
                .andExpect(status().isOk())
                .andExpect(view().name("myreservations"))
                .andExpect(model().attribute("reservations", RESERVATION_VIEW_LIST));

        verify(reservationService).getReservationsOfUser(TEST_USER_NAME);
        verify(reservationViewTransformer).transformToReservationViews(RESERVATION_MODEL_LIST);
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "ADMIN")
    public void testAdminUserCanNotNavigateToMyReservationsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/my-reservations"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "ADMIN")
    public void testAdminUserCanNotAttemptToCancelOwnReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/my-reservations/cancel-reservation")
                .flashAttr("reservationId", RESERVATION_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER", username = TEST_USER_NAME)
    public void testCancelReservationShouldRedirectToMyReservationsPageWithGenericErrorIfCancelReservationThrowsInvalidTokenException() throws Exception {
        doThrow(InvalidTokenException.class).when(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/my-reservations/cancel-reservation")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(redirectedUrl("/hotelbooking/my-reservations?error"));

        verify(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);
    }

    @Test
    @WithMockUser(authorities = "USER", username = TEST_USER_NAME)
    public void testCancelReservationShouldRedirectToMyReservationsPageWithGenericErrorIfCancelReservationThrowsInvalidReservationException() throws Exception {
        doThrow(InvalidReservationException.class).when(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/my-reservations/cancel-reservation")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(redirectedUrl("/hotelbooking/my-reservations?error"));

        verify(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);
    }

    @Test
    @WithMockUser(authorities = "USER", username = TEST_USER_NAME)
    public void testCancelReservationShouldRedirectToMyReservationsPageWithGenericErrorIfCancelReservationThrowsInvalidUserException() throws Exception {
        doThrow(InvalidUserException.class).when(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/my-reservations/cancel-reservation")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(redirectedUrl("/hotelbooking/my-reservations?error"));

        verify(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);
    }

    @Test
    @WithMockUser(authorities = "USER", username = TEST_USER_NAME)
    public void testCancelReservationShouldRedirectToMyReservationsPageWithGenericErrorIfCancelReservationThrowsAnyException() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/my-reservations/cancel-reservation")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(redirectedUrl("/hotelbooking/my-reservations?error"));

        verify(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);
    }


    @Test
    @WithMockUser(authorities = "USER", username = TEST_USER_NAME)
    public void testCancelReservationShouldRedirectToMyReservationsPageIfNoExceptionOccurredWhileCancellingReservation() throws Exception {
        doNothing().when(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/my-reservations/cancel-reservation")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(redirectedUrl("/hotelbooking/my-reservations"));

        verify(reservationService).cancelReservation(TEST_UUID, TEST_USER_NAME);
    }
}
