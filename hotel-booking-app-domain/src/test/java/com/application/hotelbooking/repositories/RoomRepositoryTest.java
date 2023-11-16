package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Hotel;
import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    public void testSaveReturnsSavedRoom(){
        Room room = Room.builder()
                .roomNumber(1)
                .roomType(RoomType.SINGLE_ROOM)
                .build();

        Room savedRoom = roomRepository.save(room);

        Assertions.assertThat(savedRoom).isNotNull();
        Assertions.assertThat(savedRoom.getId()).isNotNull();
        Assertions.assertThat(savedRoom.getRoomNumber()).isEqualTo(room.getRoomNumber());
        Assertions.assertThat(savedRoom.getRoomType()).isEqualTo(room.getRoomType());
    }
    @Test
    public void testFindByIdReturnsOptionalOfRoomWithProvidedId(){
        Hotel hotel = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Room room = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel)
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
    public void testFindRoomByRoomNumberAndHotelHotelNameShouldReturnDesiredRoom(){
        Hotel hotel1 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Hotel hotel2 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 2")
                .city("City 2")
                .build());
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel1)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .hotel(hotel1)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel2)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        Room resultRoom1 = roomRepository.findRoomByRoomNumberAndHotelHotelName(1, "Hotel 1");
        Room resultRoom2 = roomRepository.findRoomByRoomNumberAndHotelHotelName(2, "Hotel 1");
        Room resultRoom3 = roomRepository.findRoomByRoomNumberAndHotelHotelName(1, "Hotel 2");

        Assertions.assertThat(resultRoom1).isNotNull();
        Assertions.assertThat(resultRoom2).isNotNull();
        Assertions.assertThat(resultRoom3).isNotNull();
        Assertions.assertThat(resultRoom1).isEqualTo(room1);
        Assertions.assertThat(resultRoom2).isEqualTo(room2);
        Assertions.assertThat(resultRoom3).isEqualTo(room3);
    }

    @Test
    public void testFindRoomsWithConditionsShouldReturnAllRoomIdsIfNoConditionsProvided(){
        Hotel hotel1 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Hotel hotel2 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 2")
                .city("City 2")
                .build());
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel1)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .hotel(hotel1)
                .roomType(RoomType.SINGLE_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .hotel(hotel2)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Long> resultRooms = roomRepository.findRoomsWithConditions(null, null, null, null, null);

        Assertions.assertThat(resultRooms).isNotNull();
        Assertions.assertThat(resultRooms).isEqualTo(List.of(room1.getId(), room2.getId(), room3.getId()));
    }

    @Test
    public void testFindRoomsWithConditionsShouldReturnRoomIdsWhenOneConditionIsProvided(){
        Hotel hotel1 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Hotel hotel2 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 2")
                .city("City 2")
                .build());
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(hotel1)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .singleBeds(1)
                .doubleBeds(1)
                .hotel(hotel1)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(hotel2)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Long> resultRooms = roomRepository.findRoomsWithConditions(null, null, null, null, "City 1");

        Assertions.assertThat(resultRooms).isNotNull();
        Assertions.assertThat(List.of(room1.getId(), room2.getId())).isEqualTo(resultRooms);
    }

    @Test
    public void testFindRoomsWithConditionsShouldReturnRoomIdsWhenAllConditionsProvided(){
        Hotel hotel1 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Hotel hotel2 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 2")
                .city("City 2")
                .build());
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(hotel1)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .singleBeds(1)
                .doubleBeds(1)
                .hotel(hotel1)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(hotel2)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Long> resultRooms = roomRepository.findRoomsWithConditions(2, 1, RoomType.FAMILY_ROOM, "Hotel 1", "City 1");

        Assertions.assertThat(resultRooms).isNotNull();
        Assertions.assertThat(List.of(room1.getId())).isEqualTo(resultRooms);
    }

    @Test
    public void testFindRoomsWithConditionsShouldReturnEmptyListIfNothingMatchesAllConditions(){
        Hotel hotel1 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 1")
                .city("City 1")
                .build());
        Hotel hotel2 = hotelRepository.save(Hotel.builder()
                .hotelName("Hotel 2")
                .city("City 2")
                .build());
        Room room1 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(hotel1)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room2 = roomRepository.save(Room.builder()
                .roomNumber(2)
                .singleBeds(1)
                .doubleBeds(1)
                .hotel(hotel1)
                .roomType(RoomType.FAMILY_ROOM)
                .build());
        Room room3 = roomRepository.save(Room.builder()
                .roomNumber(1)
                .singleBeds(2)
                .doubleBeds(1)
                .hotel(hotel2)
                .roomType(RoomType.SINGLE_ROOM)
                .build());

        List<Long> resultRooms = roomRepository.findRoomsWithConditions(999, 1, RoomType.FAMILY_ROOM, "Hotel 1", "City 1");

        Assertions.assertThat(resultRooms).isNotNull();
        Assertions.assertThat(resultRooms).isEmpty();
    }
}
