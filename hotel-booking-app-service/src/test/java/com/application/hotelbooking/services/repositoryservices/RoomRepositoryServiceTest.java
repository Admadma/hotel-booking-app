package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.repositories.RoomRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.RoomRepositoryServiceImpl;
import com.application.hotelbooking.transformers.RoomTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomRepositoryServiceTest {

    public static final String HOTEL_NAME = "Test hotel";
    public static final String NONEXISTENT_HOTEL_NAME = "Test hotel";
    public static final String CITY = "Test city";
    public static final HotelModel HOTEL_MODEL = HotelModel.builder().hotelName(HOTEL_NAME).build();
    public static final Hotel HOTEL = Hotel.builder().hotelName(HOTEL_NAME).build();
    public static final long ROOM_ID = 1l;
    public static final int ROOM_NUMBER = 1;
    public static final RoomModel ROOM_MODEL = RoomModel.builder().roomNumber(ROOM_NUMBER).build();
    public static final Room ROOM = Room.builder().roomNumber(ROOM_NUMBER).build();
    public static final int NONEXISTENT_ROOM_NUMBER = 1;
    public static final Optional<Room> EMPTY_ROOM = Optional.empty();
    public static final Optional<RoomModel> TRANSFORMED_EMPTY_ROOM = Optional.empty();
    public static final Optional<Room> FOUND_ROOM = Optional.of(Room.builder().id(ROOM_ID).roomNumber(ROOM_NUMBER).hotel(HOTEL).build());
    public static final Optional<RoomModel> TRANSFORMED_ROOM = Optional.of(RoomModel.builder().id(ROOM_ID).roomNumber(ROOM_NUMBER).hotel(HOTEL_MODEL).build());

    @InjectMocks
    private RoomRepositoryServiceImpl roomRepositoryService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomTransformer roomTransformer;

    @Test
    public void testGetRoomByIdShouldReturnOptionalOfRoomModelIfRoomExists(){
        when(roomRepository.findById(ROOM_ID)).thenReturn(FOUND_ROOM);
        when(roomTransformer.transformToOptionalRoomModel(FOUND_ROOM)).thenReturn(TRANSFORMED_ROOM);

        Optional<RoomModel> resultRoomModel = roomRepositoryService.getRoomById(ROOM_ID);

        verify(roomRepository).findById(ROOM_ID);
        verify(roomTransformer).transformToOptionalRoomModel(FOUND_ROOM);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isNotEmpty();
        Assertions.assertThat(resultRoomModel.get().getId()).isEqualTo(ROOM_ID);
    }

    @Test
    public void testGetRoomByIdShouldReturnEmptyOptionalIfRoomDoesNotExist(){
        when(roomRepository.findById(ROOM_ID)).thenReturn(EMPTY_ROOM);
        when(roomTransformer.transformToOptionalRoomModel(EMPTY_ROOM)).thenReturn(TRANSFORMED_EMPTY_ROOM);

        Optional<RoomModel> resultRoomModel = roomRepositoryService.getRoomById(ROOM_ID);

        verify(roomRepository).findById(ROOM_ID);
        verify(roomTransformer).transformToOptionalRoomModel(EMPTY_ROOM);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isEmpty();
    }

    @Test
    public void testFindRoomByNumberAndHotelNameShouldReturnOptionalOfRoomModelIfRoomFound(){
        when(roomRepository.findRoomByRoomNumberAndHotelHotelName(ROOM_NUMBER, HOTEL_NAME)).thenReturn(FOUND_ROOM);
        when(roomTransformer.transformToOptionalRoomModel(FOUND_ROOM)).thenReturn(TRANSFORMED_ROOM);

        Optional<RoomModel> resultRoomModel = roomRepositoryService.findRoomByNumberAndHotelName(ROOM_NUMBER, HOTEL_NAME);

        verify(roomRepository).findRoomByRoomNumberAndHotelHotelName(ROOM_NUMBER, HOTEL_NAME);
        verify(roomTransformer).transformToOptionalRoomModel(FOUND_ROOM);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isNotEmpty();
        Assertions.assertThat(resultRoomModel.get().getRoomNumber()).isEqualTo(ROOM_NUMBER);
        Assertions.assertThat(resultRoomModel.get().getHotel().getHotelName()).isEqualTo(HOTEL_NAME);
    }

    @Test
    public void testFindRoomByNumberAndHotelNameShouldReturnEmptyOptionalIfGivenHotelHasNoRoomWithGivenNumber(){
        when(roomRepository.findRoomByRoomNumberAndHotelHotelName(NONEXISTENT_ROOM_NUMBER, HOTEL_NAME)).thenReturn(EMPTY_ROOM);
        when(roomTransformer.transformToOptionalRoomModel(EMPTY_ROOM)).thenReturn(TRANSFORMED_EMPTY_ROOM);

        Optional<RoomModel> resultRoomModel = roomRepositoryService.findRoomByNumberAndHotelName(NONEXISTENT_ROOM_NUMBER, HOTEL_NAME);

        verify(roomRepository).findRoomByRoomNumberAndHotelHotelName(NONEXISTENT_ROOM_NUMBER, HOTEL_NAME);
        verify(roomTransformer).transformToOptionalRoomModel(EMPTY_ROOM);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isEmpty();
    }

    @Test
    public void testFindRoomByNumberAndHotelNameShouldReturnEmptyOptionalIfGivenHotelDoesNotExist(){
        when(roomRepository.findRoomByRoomNumberAndHotelHotelName(ROOM_NUMBER, NONEXISTENT_HOTEL_NAME)).thenReturn(EMPTY_ROOM);
        when(roomTransformer.transformToOptionalRoomModel(EMPTY_ROOM)).thenReturn(TRANSFORMED_EMPTY_ROOM);

        Optional<RoomModel> resultRoomModel = roomRepositoryService.findRoomByNumberAndHotelName(ROOM_NUMBER, NONEXISTENT_HOTEL_NAME);

        verify(roomRepository).findRoomByRoomNumberAndHotelHotelName(ROOM_NUMBER, NONEXISTENT_HOTEL_NAME);
        verify(roomTransformer).transformToOptionalRoomModel(EMPTY_ROOM);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isEmpty();
    }

    @Test
    public void testSaveRoomShouldReturnRoomModelOfSavedRoom(){
        RoomCreationServiceDTO roomCreationServiceDTO = RoomCreationServiceDTO.builder().roomNumber(ROOM_NUMBER).build();

        when(roomTransformer.transformToRoom(roomCreationServiceDTO)).thenReturn(ROOM);
        when(roomRepository.save(ROOM)).thenReturn(ROOM);
        when(roomTransformer.transformToRoomModel(ROOM)).thenReturn(ROOM_MODEL);

        RoomModel resultRoomModel = roomRepositoryService.saveRoom(roomCreationServiceDTO);

        verify(roomTransformer).transformToRoom(roomCreationServiceDTO);
        verify(roomRepository).save(ROOM);
        verify(roomTransformer).transformToRoomModel(ROOM);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel.getRoomNumber()).isEqualTo(ROOM_NUMBER);
    }

    @Test
    public void testUpdateRoomShouldReturnRoomModelOfUpdatedRoom(){
        when(roomTransformer.transformToRoom(ROOM_MODEL)).thenReturn(ROOM);
        when(roomRepository.save(ROOM)).thenReturn(ROOM);
        when(roomTransformer.transformToRoomModel(ROOM)).thenReturn(ROOM_MODEL);

        RoomModel resultRoomModel = roomRepositoryService.updateRoom(ROOM_MODEL);

        verify(roomTransformer).transformToRoom(ROOM_MODEL);
        verify(roomRepository).save(ROOM);
        verify(roomTransformer).transformToRoomModel(ROOM);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel.getRoomNumber()).isEqualTo(ROOM_NUMBER);
    }

    @Test
    public void testGetRoomsWithConditionsShouldCallFindRoomsWithConditionsWithProvidedParameters(){
        RoomSearchFormServiceDTO roomSearchFormServiceDTO = new RoomSearchFormServiceDTO(1, 1, RoomType.FAMILY_ROOM, HOTEL_NAME, CITY, null, null);
        List<Long> ids = List.of(1l);
        when(roomRepository.findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                roomSearchFormServiceDTO.getRoomType(),
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity()))
                .thenReturn(ids);

        List<Long> resultIds = roomRepositoryService.getRoomsWithConditions(roomSearchFormServiceDTO);

        verify(roomRepository).findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                roomSearchFormServiceDTO.getRoomType(),
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity());
        Assertions.assertThat(resultIds).isNotNull();
        Assertions.assertThat(resultIds).isEqualTo(ids);
    }
}
