package com.application.hotelbooking.controllers;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.dto.ReviewDTO;
import com.application.hotelbooking.security.SecurityConfiguration;
import com.application.hotelbooking.services.ReviewService;
import com.application.hotelbooking.services.repositoryservices.ReservationRepositoryService;
import com.application.hotelbooking.transformers.ReservationViewTransformer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@Import(SecurityConfiguration.class)
@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    private static final UUID TEST_UUID = UUID.fromString("2a167ea9-850c-4059-8163-6f941561c419");
    private static final LocalDate START_DATE = LocalDate.of(2024, 3, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 3, 5);
    private static final String HOTEL_NAME = "Hotel 1";
    private static final HotelModel HOTEL_MODEL = HotelModel.builder().hotelName(HOTEL_NAME).build();
    private static final String IMAGE_NAME = "test_image.png";
    private static final HotelView HOTEL_VIEW = HotelView.builder().hotelName(HOTEL_NAME).imageName(IMAGE_NAME).build();
    private static final String USER_NAME = "User 1";
    private static final UserModel USER_MODEL = UserModel.builder().username(USER_NAME).build();
    private static final UserView USER_VIEW = UserView.builder().username(USER_NAME).build();
    private static final int RATING = 5;
    private static final String COMMENT = "Some comment";

    private static final RoomModel ROOM_MODEL = RoomModel.builder()
            .roomNumber(1)
            .pricePerNight(10)
            .hotel(HOTEL_MODEL)
            .build();
    private static final RoomView ROOM_VIEW = RoomView.builder()
            .roomNumber(1)
            .pricePerNight(10)
            .hotel(HOTEL_VIEW)
            .build();
    private static final ReviewModel REVIEW_MODEL = ReviewModel.builder()
            .rating(RATING)
            .comment(COMMENT)
            .hotel(HOTEL_MODEL)
            .user(USER_MODEL)
            .build();
    private static final ReservationModel PLANNED_RESERVATION_MODEL = ReservationModel.builder()
            .uuid(TEST_UUID)
            .room(ROOM_MODEL)
            .user(USER_MODEL)
            .totalPrice(100)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .reservationStatus(ReservationStatus.PLANNED)
            .build();
    private static final ReservationView PLANNED_RESERVATION_VIEW = ReservationView.builder()
            .uuid(TEST_UUID)
            .room(ROOM_VIEW)
            .user(USER_VIEW)
            .totalPrice(100)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .reservationStatus(ReservationStatus.PLANNED)
            .build();
    private static final Optional<ReservationModel> OPTIONAL_PLANNED_RESERVATION_MODEL = Optional.of(PLANNED_RESERVATION_MODEL);
    private static final ReservationModel ACTIVE_RESERVATION_MODEL = ReservationModel.builder()
            .uuid(TEST_UUID)
            .room(ROOM_MODEL)
            .user(USER_MODEL)
            .totalPrice(100)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .reservationStatus(ReservationStatus.ACTIVE)
            .build();
    private static final ReservationView ACTIVE_RESERVATION_VIEW = ReservationView.builder()
            .uuid(TEST_UUID)
            .room(ROOM_VIEW)
            .user(USER_VIEW)
            .totalPrice(100)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .reservationStatus(ReservationStatus.ACTIVE)
            .build();

    private static final Optional<ReservationModel> OPTIONAL_ACTIVE_RESERVATION_MODEL = Optional.of(ACTIVE_RESERVATION_MODEL);
    private static final ReservationModel COMPLETED_RESERVATION_MODEL = ReservationModel.builder()
            .uuid(TEST_UUID)
            .room(ROOM_MODEL)
            .user(USER_MODEL)
            .totalPrice(100)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .reservationStatus(ReservationStatus.COMPLETED)
            .build();
    private static final ReservationView COMPLETED_RESERVATION_VIEW = ReservationView.builder()
            .uuid(TEST_UUID)
            .room(ROOM_VIEW)
            .user(USER_VIEW)
            .totalPrice(100)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .reservationStatus(ReservationStatus.COMPLETED)
            .build();
    private static final Optional<ReservationModel> OPTIONAL_COMPLETED_RESERVATION_MODEL = Optional.of(COMPLETED_RESERVATION_MODEL);
    private static final ReviewDTO REVIEW_DTO = ReviewDTO.builder().rating(5).comment("Lorem ipsum dolor sit amet, consectetur adipiscing elit.").build();
    private static final ReviewDTO EMPTY_REVIEW_DTO = new ReviewDTO();
    private static final ReviewDTO REVIEW_DTO_NO_RATING_TOO_LONG_COMMENT = ReviewDTO.builder().comment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce vel purus sit amet nisi fringilla varius eget id purus. Duis quis orci at eros tempor congue. Aliquam a orci leo. Nullam at justo nec odio consectetur facilisis vel non sem. Integer rhoncus, ipsum sed tempor commodo, justo elit tempor dui, nec semper justo arcu sed tortor.").build();


    @MockBean
    private ReviewService reviewService;
    @MockBean
    private ReservationRepositoryService reservationRepositoryService;
    @MockBean
    private ReservationViewTransformer reservationViewTransformer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testAdminCanNotNavigateToReviewPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/makeReview")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN", username = "admin")
    public void testAdminCanNotAttemptToSubmitReview() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/submit-review")
                        .flashAttr("reviewDTO", REVIEW_DTO)
                        .sessionAttr("hotelName", HOTEL_NAME))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testUserCanNavigateToReviewPageIfTheirSelectedReservationIsCompleted() throws Exception {
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_COMPLETED_RESERVATION_MODEL);
        when(reservationViewTransformer.transformToReservationView(COMPLETED_RESERVATION_MODEL)).thenReturn(COMPLETED_RESERVATION_VIEW);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/makeReview")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(status().isOk())
                .andExpect(view().name("reviewpage"))
                .andExpect(model().attributeExists("reviewDTO"))
                .andExpect(model().attribute("reviewDTO", EMPTY_REVIEW_DTO))
                .andExpect(request().sessionAttribute("hotelName", HOTEL_NAME))
                .andExpect(request().sessionAttribute("hotelImageName", IMAGE_NAME));

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
        verify(reservationViewTransformer).transformToReservationView(COMPLETED_RESERVATION_MODEL);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testUserCanNavigateToReviewPageButRedirectedToMyReservationsPageIfTheSelectedReservationIsInPlannedStatus() throws Exception {
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_PLANNED_RESERVATION_MODEL);
        when(reservationViewTransformer.transformToReservationView(PLANNED_RESERVATION_MODEL)).thenReturn(PLANNED_RESERVATION_VIEW);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/makeReview")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(redirectedUrl("/hotelbooking/myreservations"))
                .andExpect(request().sessionAttributeDoesNotExist("hotelName"))
                .andExpect(request().sessionAttributeDoesNotExist("hotelImageName"))
                .andExpect(model().attributeDoesNotExist("reviewDTO"));

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
        verify(reservationViewTransformer).transformToReservationView(PLANNED_RESERVATION_MODEL);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testUserCanNavigateToReviewPageButRedirectedToMyReservationsPageIfTheSelectedReservationIsInActiveStatus() throws Exception {
        when(reservationRepositoryService.getReservationByUuid(TEST_UUID)).thenReturn(OPTIONAL_ACTIVE_RESERVATION_MODEL);
        when(reservationViewTransformer.transformToReservationView(ACTIVE_RESERVATION_MODEL)).thenReturn(ACTIVE_RESERVATION_VIEW);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hotelbooking/makeReview")
                        .flashAttr("reservationUuid", TEST_UUID))
                .andExpect(redirectedUrl("/hotelbooking/myreservations"))
                .andExpect(request().sessionAttributeDoesNotExist("hotelName"))
                .andExpect(request().sessionAttributeDoesNotExist("hotelImageName"))
                .andExpect(model().attributeDoesNotExist("reviewDTO"));

        verify(reservationRepositoryService).getReservationByUuid(TEST_UUID);
        verify(reservationViewTransformer).transformToReservationView(ACTIVE_RESERVATION_MODEL);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testSubmitReviewShouldReturnToReviewPageIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/submit-review")
                        .flashAttr("reviewDTO", REVIEW_DTO_NO_RATING_TOO_LONG_COMMENT))
                .andExpect(status().isOk())
                .andExpect(view().name("reviewpage"))
                .andExpect(model().attributeErrorCount("reviewDTO", 2));
    }

    @Test
    @WithMockUser(authorities = "USER", username = USER_NAME)
    public void testSubmitReviewShouldCallToCreateTheNewReviewAndReturnToReviewPage() throws Exception {
        when(reviewService.createReview(REVIEW_DTO.getRating(), REVIEW_DTO.getComment(), HOTEL_NAME, USER_NAME)).thenReturn(REVIEW_MODEL);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/submit-review")
                        .flashAttr("reviewDTO", REVIEW_DTO)
                        .sessionAttr("hotelName", HOTEL_NAME))
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("reviewDTO", 0))
                .andExpect(view().name("reviewpage"));

        verify(reviewService).createReview(REVIEW_DTO.getRating(), REVIEW_DTO.getComment(), HOTEL_NAME, USER_NAME);
    }

    @Test
    @WithMockUser(authorities = "USER", username = USER_NAME)
    public void testSubmitReviewShouldReturnToReviewPageIfAnyExceptionHappenedDuringReviewCreation() throws Exception {
        when(reviewService.createReview(REVIEW_DTO.getRating(), REVIEW_DTO.getComment(), HOTEL_NAME, USER_NAME)).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hotelbooking/submit-review")
                        .flashAttr("reviewDTO", REVIEW_DTO)
                        .sessionAttr("hotelName", HOTEL_NAME))
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("reviewDTO", 0))
                .andExpect(view().name("reviewpage"));

        verify(reviewService).createReview(REVIEW_DTO.getRating(), REVIEW_DTO.getComment(), HOTEL_NAME, USER_NAME);
    }
}
