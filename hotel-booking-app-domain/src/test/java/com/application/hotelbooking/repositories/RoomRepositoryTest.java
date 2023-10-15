package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testfindRoomByIdShouldReturnRoomWithGivenId(){
        //Given
        Room room = Room.builder()
                .roomNumber(10)
                .singleBeds(1)
                .roomType(RoomType.SINGLE_ROOM)
                .build();
        roomRepository.save(room);

        //When
        Room result = roomRepository.findRoomById(1l);

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(room.getRoomNumber(), result.getRoomNumber());
        Assertions.assertEquals(room.getSingleBeds(), result.getSingleBeds());
        Assertions.assertEquals(room.getRoomType(), result.getRoomType());
    }

}
