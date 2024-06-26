package com.application.hotelbooking.repositories;

import com.application.hotelbooking.entities.Hotel;
import com.application.hotelbooking.entities.Room;
import com.application.hotelbooking.entities.RoomType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RoomRepositoryTest {

    private static final Hotel HOTEL_ONE = Hotel.builder()
            .hotelName("Hotel 1")
            .city("City 1")
            .imageName("image.png")
            .averageRating(0.0)
            .build();
    private static final Hotel HOTEL_TWO = Hotel.builder()
            .hotelName("Hotel 2")
            .city("City 2")
            .imageName("image.png")
            .averageRating(0.0)
            .build();

    private Hotel SAVED_HOTEL_ONE;
    private Hotel SAVED_HOTEL_TWO;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        SAVED_HOTEL_ONE = hotelRepository.save(HOTEL_ONE);
        SAVED_HOTEL_TWO = hotelRepository.save(HOTEL_TWO);
    }

    @Test
    public void testSaveReturnsSavedRoom(){
        Room room = Room.builder()
                .roomNumber(1)
                .roomType(RoomType.SINGLE_ROOM)
                .hotel(SAVED_HOTEL_ONE)
                .build();

        Room savedRoom = roomRepository.save(room);

        Assertions.assertThat(savedRoom).isNotNull();
        Assertions.assertThat(savedRoom.getId()).isNotNull();
        Assertions.assertThat(savedRoom.getRoomNumber()).isEqualTo(room.getRoomNumber());
        Assertions.assertThat(savedRoom.getRoomType()).isEqualTo(room.getRoomType());
    }
    @Test
    public void testFindByIdReturnsOptionalOfRoomWithProvidedId(){
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        Optional<Room> resultRoom = roomRepository.findById(room.getId());

        Assertions.assertThat(resultRoom).isNotNull();
        Assertions.assertThat(resultRoom).isNotEmpty();
        Assertions.assertThat(resultRoom.get().getRoomNumber()).isEqualTo(room.getRoomNumber());
        Assertions.assertThat(resultRoom.get().getHotel().getHotelName()).isEqualTo(room.getHotel().getHotelName());
    }

    @Test
    public void testFindByIdReturnsEmptyOptionalWithNonexistentIdProvided(){

        Optional<Room> resultRoom = roomRepository.findById(1l);

        Assertions.assertThat(resultRoom).isNotNull();
        Assertions.assertThat(resultRoom).isEmpty();
    }

    @Test
    public void testFindRoomByRoomNumberAndHotelHotelNameShouldReturnOptionalOfDesiredRoom(){
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_TWO)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        Optional<Room> resultRoom1 = roomRepository.findRoomByRoomNumberAndHotelHotelName(1, "Hotel 1");
        Optional<Room> resultRoom2 = roomRepository.findRoomByRoomNumberAndHotelHotelName(2, "Hotel 1");
        Optional<Room> resultRoom3 = roomRepository.findRoomByRoomNumberAndHotelHotelName(1, "Hotel 2");

        Assertions.assertThat(resultRoom1).isNotNull();
        Assertions.assertThat(resultRoom2).isNotNull();
        Assertions.assertThat(resultRoom3).isNotNull();
        Assertions.assertThat(resultRoom1).isNotEmpty();
        Assertions.assertThat(resultRoom2).isNotEmpty();
        Assertions.assertThat(resultRoom3).isNotEmpty();
        Assertions.assertThat(resultRoom1.get()).isEqualTo(room1);
        Assertions.assertThat(resultRoom2.get()).isEqualTo(room2);
        Assertions.assertThat(resultRoom3.get()).isEqualTo(room3);
    }

    @Test
    public void testFindRoomsWithConditionsShouldReturnAllRoomIdsIfNoConditionsProvided(){
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(SAVED_HOTEL_TWO)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Long> resultRooms = roomRepository.findRoomsWithConditions(null, null, null, null, null);

        Assertions.assertThat(resultRooms).isNotNull();
        Assertions.assertThat(resultRooms).isEqualTo(List.of(room1.getId(), room2.getId(), room3.getId()));
    }

    @Test
    public void testFindRoomsWithConditionsShouldReturnRoomIdsWhenOneConditionIsProvided(){
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .singleBeds(1)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_TWO)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Long> resultRooms = roomRepository.findRoomsWithConditions(null, null, null, null, "City 1");

        Assertions.assertThat(resultRooms).isNotNull();
        Assertions.assertThat(resultRooms).isEqualTo(List.of(room1.getId(), room2.getId()));
    }

    @Test
    public void testFindRoomsWithConditionsShouldReturnRoomIdsWhenAllConditionsProvided(){
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .singleBeds(1)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_TWO)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Long> resultRooms = roomRepository.findRoomsWithConditions(2, 1, RoomType.FAMILY_ROOM, "Hotel 1", "City 1");

        Assertions.assertThat(resultRooms).isNotNull();
        Assertions.assertThat(resultRooms).isEqualTo(List.of(room1.getId()));
    }

    @Test
    public void testFindRoomsWithConditionsShouldReturnEmptyListIfNothingMatchesAllConditions(){
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .singleBeds(1)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_ONE)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(SAVED_HOTEL_TWO)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Long> resultRooms = roomRepository.findRoomsWithConditions(999, 1, RoomType.FAMILY_ROOM, "Hotel 1", "City 1");

        Assertions.assertThat(resultRooms).isNotNull();
        Assertions.assertThat(resultRooms).isEmpty();
    }
}
