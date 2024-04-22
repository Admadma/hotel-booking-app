package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class ReservationRepositoryTest {

    private static final Hotel HOTEL_ONE = Hotel.builder()
            .hotelName("Hotel 1")
            .city("City 1")
            .imageName("image.png")
            .averageRating(0.0)
            .build();
    private static final User USER = User.builder()
            .username("TEST_USER_NAME")
            .password("TEST_PASSWORD")
            .email("TEST_USER_EMAIL")
            .enabled(true)
            .locked(false)
            .build();

    private static final UUID UUID_ONE = UUID.randomUUID();
    private static final UUID UUID_TWO = UUID.randomUUID();
    private static final LocalDate START_DATE_ONE = LocalDate.of(2024, 3, 1);
    private static final LocalDate END_DATE_ONE = LocalDate.of(2024, 3, 2);
    private static final LocalDate START_DATE_TWO = LocalDate.of(2024, 3, 11);
    private static final LocalDate END_DATE_TWO = LocalDate.of(2024, 3, 12);

    private Hotel SAVED_HOTEL_ONE;
    private User SAVED_USER;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SAVED_HOTEL_ONE = hotelRepository.save(HOTEL_ONE);
        SAVED_USER = userRepository.save(USER);
    }

    @Test
    public void testSaveReturnsSavedReservation(){
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Reservation reservation = Reservation.builder()
                .uuid(UUID_ONE)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_ONE)
                .endDate(END_DATE_ONE)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        Assertions.assertThat(savedReservation).isNotNull();
        Assertions.assertThat(savedReservation.getId()).isNotNull();
        Assertions.assertThat(savedReservation).isEqualTo(reservation);
    }

    @Test
    public void testFindAllByRoomIdReturnsAllReservationsOnGivenRoom(){
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .uuid(UUID_ONE)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_ONE)
                .endDate(END_DATE_ONE)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build());
        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .uuid(UUID_TWO)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_TWO)
                .endDate(END_DATE_TWO)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build());

        List<Reservation> resultReservation = reservationRepository.findAllByRoomId(room.getId());

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isNotEmpty();
        Assertions.assertThat(resultReservation).isEqualTo(List.of(reservation1, reservation2));
    }

    @Test
    public void testFindAllByRoomIdReturnsEmptyListIfRoomHasNoReservations(){
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Reservation> resultReservation = reservationRepository.findAllByRoomId(room.getId());

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isEmpty();
    }
    @Test
    public void testFindAllByRoomIdReturnsEmptyListIfRoomDoesNotExist(){

        List<Reservation> resultReservation = reservationRepository.findAllByRoomId(1l);

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isEmpty();
    }

    @Test
    public void testFindAllByUserReturnsAllReservationsOfGivenUser(){
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .uuid(UUID_ONE)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_ONE)
                .endDate(END_DATE_ONE)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build());
        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .uuid(UUID_TWO)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_TWO)
                .endDate(END_DATE_TWO)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build());

        List<Reservation> resultReservation = reservationRepository.findAllByUserId(SAVED_USER.getId());

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isNotEmpty();
        Assertions.assertThat(resultReservation).isEqualTo(List.of(reservation1, reservation2));
    }

    @Test
    public void testFindAllByUserReturnsEmptyListIfUserHasNoReservations(){

        List<Reservation> resultReservation = reservationRepository.findAllByUserId(SAVED_USER.getId());

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isEmpty();
    }
    @Test
    public void testFindAllByUserReturnsEmptyListIfUserDoesNotExist(){

        List<Reservation> resultReservation = reservationRepository.findAllByUserId(1l);

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isEmpty();
    }

    @Test
    public void testFindByUuidShouldReturnReservationWithGivenUUID(){
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .uuid(UUID_ONE)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_ONE)
                .endDate(END_DATE_ONE)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build());
        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .uuid(UUID_TWO)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_TWO)
                .endDate(END_DATE_TWO)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build());

        Optional<Reservation> resultReservation = reservationRepository.findByUuid(UUID_ONE);

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isNotEmpty();
        Assertions.assertThat(resultReservation.get()).isEqualTo(reservation1);
    }

    @Test
    public void testDeleteByIdShouldDeleteGivenReservation(){
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .uuid(UUID_ONE)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_ONE)
                .endDate(END_DATE_ONE)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build());
        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .uuid(UUID_TWO)
                .room(room)
                .user(SAVED_USER)
                .startDate(START_DATE_TWO)
                .endDate(END_DATE_TWO)
                .totalPrice(100)
                .reservationStatus(ReservationStatus.COMPLETED)
                .build());

        reservationRepository.deleteById(reservation1.getId());
        List<Reservation> savedReservations = reservationRepository.findAll();

        Assertions.assertThat(savedReservations).isNotNull();
        Assertions.assertThat(savedReservations).isNotEmpty();
        Assertions.assertThat(savedReservations).isEqualTo(List.of(reservation2));
    }
}
