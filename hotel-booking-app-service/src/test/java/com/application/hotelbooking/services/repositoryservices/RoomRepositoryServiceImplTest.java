package com.application.hotelbooking.services.repositoryservices;

import com.application.hotelbooking.entities.*;
import com.application.hotelbooking.domain.*;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.repositories.RoomRepository;
import com.application.hotelbooking.services.repositoryservices.implementations.RoomRepositoryServiceImpl;
import com.application.hotelbooking.transformers.RoomTransformer;
import com.application.hotelbooking.transformers.RoomTypeTransformer;
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
public class RoomRepositoryServiceImplTest {

    private static final String HOTEL_NAME = "Test hotel";
    private static final String NONEXISTENT_HOTEL_NAME = "Test hotel";
    private static final String CITY = "Test city";
    private static final com.application.hotelbooking.domain.RoomType FAMILY_ROOM_MODEL = com.application.hotelbooking.domain.RoomType.FAMILY_ROOM;
    private static final com.application.hotelbooking.entities.RoomType FAMILY_ROOM_ENTITY = com.application.hotelbooking.entities.RoomType.FAMILY_ROOM;
    private static final HotelModel HOTEL_MODEL = HotelModel.builder().hotelName(HOTEL_NAME).build();
    private static final Hotel HOTEL = Hotel.builder().hotelName(HOTEL_NAME).build();
    private static final long ROOM_ID = 1l;
    private static final int ROOM_NUMBER = 1;
    private static final long VERSION = 0l;
    private static final RoomModel ROOM_MODEL = RoomModel.builder().roomNumber(ROOM_NUMBER).version(VERSION).build();
    private static final Room ROOM = Room.builder().roomNumber(ROOM_NUMBER).build();
    private static final int NONEXISTENT_ROOM_NUMBER = 1;
    private static final Optional<Room> EMPTY_ROOM = Optional.empty();
    private static final Optional<RoomModel> TRANSFORMED_EMPTY_ROOM = Optional.empty();
    private static final Optional<Room> FOUND_ROOM = Optional.of(Room.builder().id(ROOM_ID).roomNumber(ROOM_NUMBER).hotel(HOTEL).build());
    private static final Optional<RoomModel> TRANSFORMED_ROOM = Optional.of(RoomModel.builder().id(ROOM_ID).roomNumber(ROOM_NUMBER).hotel(HOTEL_MODEL).build());
    private static final List<Long> RESULT_IDS = List.of(1l);

    @InjectMocks
    private RoomRepositoryServiceImpl roomRepositoryService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomTransformer roomTransformer;
    @Mock
    private RoomTypeTransformer roomTypeTransformer;


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

        RoomModel savedRoom = roomRepositoryService.saveRoom(roomCreationServiceDTO);

        verify(roomTransformer).transformToRoom(roomCreationServiceDTO);
        verify(roomRepository).save(ROOM);
        verify(roomTransformer).transformToRoomModel(ROOM);
        Assertions.assertThat(savedRoom).isNotNull();
        Assertions.assertThat(savedRoom.getRoomNumber()).isEqualTo(ROOM_NUMBER);
    }

    @Test
    public void testIncrementRoomVersionShouldIncreaseVersionOfRoomModelByOne(){
        RoomModel copyOfRoomModel = RoomModel.builder().roomNumber(ROOM_MODEL.getRoomNumber()).version(ROOM_MODEL.getVersion()).build();
        when(roomTransformer.transformToRoom(copyOfRoomModel)).thenReturn(ROOM);
        when(roomRepository.save(ROOM)).thenReturn(ROOM);

        roomRepositoryService.incrementRoomVersion(copyOfRoomModel);

        verify(roomTransformer).transformToRoom(copyOfRoomModel);
        verify(roomRepository).save(ROOM);
        Assertions.assertThat(copyOfRoomModel).isNotNull();
        Assertions.assertThat(copyOfRoomModel.getVersion()).isEqualTo(ROOM_MODEL.getVersion() + 1);
    }

    @Test
    public void testGetRoomsWithConditionsShouldCallFindRoomsWithConditionsWithProvidedRoomSearchFormServiceDTOAttributes(){
        RoomSearchFormServiceDTO roomSearchFormServiceDTO = new RoomSearchFormServiceDTO(1, 1, FAMILY_ROOM_MODEL, HOTEL_NAME, CITY, null, null);
        when(roomTypeTransformer.transformToRoomTypeEntity(roomSearchFormServiceDTO.getRoomType())).thenReturn(FAMILY_ROOM_ENTITY);
        when(roomRepository.findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                FAMILY_ROOM_ENTITY,
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity()))
                .thenReturn(RESULT_IDS);

        List<Long> resultIds = roomRepositoryService.getRoomsWithConditions(roomSearchFormServiceDTO);

        verify(roomTypeTransformer).transformToRoomTypeEntity(roomSearchFormServiceDTO.getRoomType());
        verify(roomRepository).findRoomsWithConditions(roomSearchFormServiceDTO.getSingleBeds(),
                roomSearchFormServiceDTO.getDoubleBeds(),
                FAMILY_ROOM_ENTITY,
                roomSearchFormServiceDTO.getHotelName(),
                roomSearchFormServiceDTO.getCity());
        Assertions.assertThat(resultIds).isNotNull();
        Assertions.assertThat(resultIds).isEqualTo(RESULT_IDS);
    }

    @Test
    public void testGetRoomsWithConditionsShouldCallFindRoomsWithConditionsWithProvidedParameters(){
        when(roomTypeTransformer.transformToRoomTypeEntity(FAMILY_ROOM_MODEL)).thenReturn(FAMILY_ROOM_ENTITY);
        when(roomRepository.findRoomsWithConditions(1, 1,FAMILY_ROOM_ENTITY, HOTEL_NAME, CITY)).thenReturn(RESULT_IDS);

        List<Long> resultIds = roomRepositoryService.getRoomsWithConditions(1, 1, FAMILY_ROOM_MODEL, HOTEL_NAME, CITY);

        verify(roomTypeTransformer).transformToRoomTypeEntity(FAMILY_ROOM_MODEL);
        verify(roomRepository).findRoomsWithConditions(1, 1, FAMILY_ROOM_ENTITY, HOTEL_NAME, CITY);
        Assertions.assertThat(resultIds).isNotNull();
        Assertions.assertThat(resultIds).isEqualTo(RESULT_IDS);
    }
}
