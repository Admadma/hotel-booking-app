package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.CheckInOutService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@Import(SecurityConfiguration.class)
@WebMvcTest(GuestCheckInOutController.class)
public class GuestCheckInOutControllerTest {

    private static final UUID TEST_UUID = UUID.fromString("2a167ea9-850c-4059-8163-6f941561c419");
    private static final LocalDate START_DATE= LocalDate.of(2024, 3, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 3, 2);
    private static final ReservationModel RESERVATION_MODEL = new ReservationModel();
    private static final UserView USER_VIEW = UserView.builder().username("TEST_USER_NAME").build();
    private static final HotelView HOTEL_VIEW = HotelView.builder().hotelName("TEST_HOTEL").build();
    private static final RoomView ROOM_VIEW = RoomView.builder().roomNumber(1).singleBeds(2).doubleBeds(1).roomType(RoomType.FAMILY_ROOM).hotel(HOTEL_VIEW).build();
    private static final ReservationView RESERVATION_VIEW = ReservationView.builder()
            .uuid(TEST_UUID)
            .room(ROOM_VIEW)
            .user(USER_VIEW)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .totalPrice(100)
            .reservationStatus(ReservationStatus.COMPLETED)
            .build();


    @MockBean
    private CheckInOutService checkInOutService;

    @MockBean
    private ReservationViewTransformer reservationViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminCanNavigateToGuestCheckInOutPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/admin/checkInOut"))
                .andExpect(status().isOk())
                .andExpect(view().name("guestcheckinout"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testUserCanNotNavigateToGuestCheckInOutPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/admin/checkInOut"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminCanRetrieveReservationInfo() throws Exception {
        when(checkInOutService.getReservationDetails(TEST_UUID)).thenReturn(RESERVATION_MODEL);
        when(reservationViewTransformer.transformToReservationView(RESERVATION_MODEL)).thenReturn(RESERVATION_VIEW);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/admin/retrieve-reservation-info")
                        .param("reservationId", String.valueOf(TEST_UUID)))
                .andExpect(status().isOk())
                .andExpect(view().name("guestcheckinout"))
                .andExpect(request().sessionAttribute("reservation", RESERVATION_VIEW));

        verify(checkInOutService).getReservationDetails(TEST_UUID);
        verify(reservationViewTransformer).transformToReservationView(RESERVATION_MODEL);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testUserCanNotRetrieveReservationInfo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/admin/retrieve-reservation-info")
                        .flashAttr("reservationId", TEST_UUID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminCanCheckInGuests() throws Exception {
        when(checkInOutService.checkInGuest(TEST_UUID)).thenReturn(RESERVATION_MODEL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/check-in")
                        .sessionAttr("reservation", RESERVATION_VIEW))
                .andExpect(status().isOk())
                .andExpect(view().name("guestcheckinout"))
                .andExpect(request().sessionAttributeDoesNotExist("reservation"));

        verify(checkInOutService).checkInGuest(TEST_UUID);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testUserCanNotCheckInGuests() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/check-in")
                        .sessionAttr("reservation", RESERVATION_VIEW))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminCanCheckOutGuests() throws Exception {
        when(checkInOutService.checkOutGuest(TEST_UUID)).thenReturn(RESERVATION_MODEL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/check-out")
                        .sessionAttr("reservation", RESERVATION_VIEW))
                .andExpect(status().isOk())
                .andExpect(view().name("guestcheckinout"))
                .andExpect(request().sessionAttributeDoesNotExist("reservation"));

        verify(checkInOutService).checkOutGuest(TEST_UUID);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testUserCanNotCheckOutGuests() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/admin/check-out")
                        .sessionAttr("reservation", RESERVATION_VIEW))
                .andExpect(status().isForbidden());
    }
}
