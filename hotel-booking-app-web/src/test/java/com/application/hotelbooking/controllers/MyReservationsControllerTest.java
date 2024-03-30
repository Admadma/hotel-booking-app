package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.ReservationService;
import com.application.hotelbooking.services.imagehandling.FileSystemStorageService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.OptimisticLockException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Import(SecurityConfiguration.class)
@WebMvcTest(MyReservationsController.class)
public class MyReservationsControllerTest {

    private static final Long RESERVATION_ID = 1L;
    private static final String TEST_USER_NAME = "test_user";
    private static final ReservationModel RESERVATION_MODEL = new ReservationModel();
    private static final List<ReservationModel> RESERVATION_MODEL_LIST = List.of(RESERVATION_MODEL);
    private static final HotelView HOTEL_VIEW = HotelView.builder()
            .hotelName("Test Hotel")
            .city("Test City")
            .build();
    private static final RoomView ROOM_VIEW = RoomView.builder()
            .roomNumber(1)
            .hotel(HOTEL_VIEW)
            .roomType(RoomType.FAMILY_ROOM)
            .build();
    private static final ReservationView RESERVATION_VIEW = ReservationView.builder()
            .room(ROOM_VIEW)
            .startDate(LocalDate.now().plusDays(5))
            .endDate(LocalDate.now().plusDays(10))
            .totalPrice(100)
            .build();
    private static final List<ReservationView> RESERVATION_VIEW_LIST = List.of(RESERVATION_VIEW);

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private ReservationViewTransformer reservationViewTransformer;

    @MockBean
    private Dotenv dotenv;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "USER")
    public void testUserCanNavigateToMyReservationsPage() throws Exception {
        when(reservationService.getReservationsOfUser(TEST_USER_NAME)).thenReturn(RESERVATION_MODEL_LIST);
        when(reservationViewTransformer.transformToReservationViews(RESERVATION_MODEL_LIST)).thenReturn(RESERVATION_VIEW_LIST);

        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/myreservations"))
                .andExpect(status().isOk())
                .andExpect(view().name("myreservations"))
                .andExpect(model().attribute("reservations", RESERVATION_VIEW_LIST));

        verify(reservationService).getReservationsOfUser(TEST_USER_NAME);
        verify(reservationViewTransformer).transformToReservationViews(RESERVATION_MODEL_LIST);
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "ADMIN")
    public void testAdminUserCanNotNavigateToMyReservationsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotelbooking/myreservations"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TEST_USER_NAME, authorities = "ADMIN")
    public void testAdminUserCanNotAttemptToCancelOwnReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/cancel-reservation")
                .flashAttr("reservationId", RESERVATION_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testCancelReservationShouldRedirectToMyReservationsPageWithGenericErrorIfCancelReservationThrowsAnyException() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(reservationService).cancelReservation(RESERVATION_ID);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/cancel-reservation")
                        .flashAttr("reservationId", RESERVATION_ID))
                .andExpect(redirectedUrl("/hotelbooking/myreservations?error"));

        verify(reservationService).cancelReservation(RESERVATION_ID);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testCancelReservationShouldRedirectToMyReservationsPageIfNoExceptionOccurredWhileCancellingReservation() throws Exception {
        doNothing().when(reservationService).cancelReservation(RESERVATION_ID);

        mockMvc.perform(MockMvcRequestBuilders.post("/hotelbooking/cancel-reservation")
                        .flashAttr("reservationId", RESERVATION_ID))
                .andExpect(redirectedUrl("/hotelbooking/myreservations"));

        verify(reservationService).cancelReservation(RESERVATION_ID);
    }
}
