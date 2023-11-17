package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveReturnsSavedReservation(){
        Hotel hotel = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());
        Reservation reservation = Reservation.builder()
                .room(room)
                .user(user)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .totalPrice(100)
                .build();


        Reservation savedReservation = reservationRepository.save(reservation);

        Assertions.assertThat(savedReservation).isNotNull();
        Assertions.assertThat(savedReservation.getId()).isNotNull();
        Assertions.assertThat(savedReservation).isEqualTo(reservation);
    }

    @Test
    public void testFindAllByRoomIdReturnsAllReservationsOnGivenRoom(){
        Hotel hotel = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());
        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .room(room)
                .user(user)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .totalPrice(100)
                .build());
        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .room(room)
                .user(user)
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(11))
                .totalPrice(100)
                .build());


        List<Reservation> resultReservation = reservationRepository.findAllByRoomId(room.getId());

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isNotEmpty();
        Assertions.assertThat(resultReservation).isEqualTo(List.of(reservation1, reservation2));
    }

    @Test
    public void testFindAllByRoomIdReturnsEmptyListIfRoomHasNoReservations(){
        Hotel hotel = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel)
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
        Hotel hotel = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());
        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .room(room)
                .user(user)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .totalPrice(100)
                .build());
        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .room(room)
                .user(user)
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(11))
                .totalPrice(100)
                .build());


        List<Reservation> resultReservation = reservationRepository.findAllByUserId(user.getId());

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isNotEmpty();
        Assertions.assertThat(resultReservation).isEqualTo(List.of(reservation1, reservation2));
    }

    @Test
    public void testFindAllByUserReturnsEmptyListIfUserHasNoReservations(){
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());

        List<Reservation> resultReservation = reservationRepository.findAllByUserId(user.getId());

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isEmpty();
    }
    @Test
    public void testFindAllByUserReturnsEmptyListIfUserDoesNotExist(){
        User user = User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build();

        List<Reservation> resultReservation = reservationRepository.findAllByUserId(1l);

        Assertions.assertThat(resultReservation).isNotNull();
        Assertions.assertThat(resultReservation).isEmpty();
    }

    @Test
    public void testDeleteByIdShouldDeleteGivenReservation(){
        Hotel hotel = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        User user = userRepository.save(User.builder()
                .username("TEST_USER_NAME")
                .password("TEST_PASSWORD")
                .email("TEST_USER_EMAIL")
                .enabled(true)
                .locked(false)
                .build());
        Reservation reservation1 = reservationRepository.save(Reservation.builder()
                .room(room)
                .user(user)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .totalPrice(100)
                .build());
        Reservation reservation2 = reservationRepository.save(Reservation.builder()
                .room(room)
                .user(user)
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(11))
                .totalPrice(100)
                .build());

        reservationRepository.deleteById(reservation1.getId());
        List<Reservation> savedReservations = reservationRepository.findAll();

        Assertions.assertThat(savedReservations).isNotNull();
        Assertions.assertThat(savedReservations).isNotEmpty();
        Assertions.assertThat(savedReservations).isEqualTo(List.of(reservation2));
    }
}
