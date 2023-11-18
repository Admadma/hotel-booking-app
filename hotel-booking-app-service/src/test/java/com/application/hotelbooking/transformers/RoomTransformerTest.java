package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Room;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomTransformerTest {

    public static final Class<Room> ROOM_CLASS = Room.class;
    public static final Room ROOM = new Room();
    public static final Optional<Room> OPTIONAL_ROOM = Optional.of(new Room());
    public static final Optional<Room> EMPTY_ROOM = Optional.empty();
    public static final Class<RoomModel> ROOM_MODEL_CLASS = RoomModel.class;
    public static final RoomModel ROOM_MODEL = new RoomModel();
    public static final Optional<RoomModel> OPTIONAL_ROOM_MODEL = Optional.of(new RoomModel());
    public static final Class<RoomCreationServiceDTO> ROOM_CREATION_SERVICE_DTO_CLASS = RoomCreationServiceDTO.class;
    public static final RoomCreationServiceDTO ROOM_CREATION_SERVICE_DTO = new RoomCreationServiceDTO();
    public static final TypeMap<RoomCreationServiceDTO, Room> TYPE_MAP = mock(TypeMap.class);

    @InjectMocks
    private RoomTransformer roomTransformer;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToRoomShouldReturnTransformedRoomCreationServiceDTO(){
        when(modelMapper.typeMap(ROOM_CREATION_SERVICE_DTO_CLASS, ROOM_CLASS)).thenReturn(TYPE_MAP);
        when(modelMapper.map(ROOM_CREATION_SERVICE_DTO, ROOM_CLASS)).thenReturn(ROOM);

        Room roomResult = roomTransformer.transformToRoom(ROOM_CREATION_SERVICE_DTO);

        verify(modelMapper).typeMap(ROOM_CREATION_SERVICE_DTO_CLASS, ROOM_CLASS);
        verify(modelMapper).map(ROOM_CREATION_SERVICE_DTO, ROOM_CLASS);
        Assertions.assertThat(roomResult).isNotNull();
        Assertions.assertThat(roomResult).isEqualTo(ROOM);
    }

    @Test
    public void testTransformToRoomShouldReturnTransformedRoomModel(){
        when(modelMapper.map(ROOM_MODEL, ROOM_CLASS)).thenReturn(ROOM);

        Room resultRoom = roomTransformer.transformToRoom(ROOM_MODEL);

        verify(modelMapper).map(ROOM_MODEL, ROOM_CLASS);
        Assertions.assertThat(resultRoom).isNotNull();
        Assertions.assertThat(resultRoom).isEqualTo(ROOM);
    }

    @Test
    public void testTransformToRoomModelShouldReturnTransformedRoom(){
        when(modelMapper.map(ROOM, ROOM_MODEL_CLASS)).thenReturn(ROOM_MODEL);

        RoomModel resultRoom = roomTransformer.transformToRoomModel(ROOM);

        verify(modelMapper).map(ROOM, ROOM_MODEL_CLASS);
        Assertions.assertThat(resultRoom).isNotNull();
        Assertions.assertThat(resultRoom).isEqualTo(ROOM_MODEL);
    }

    @Test
    public void testTransformToOptionalRoomModelShouldReturnOptionalOfPresentRoomModel(){
        when(modelMapper.map(OPTIONAL_ROOM, ROOM_MODEL_CLASS)).thenReturn(ROOM_MODEL);

        Optional<RoomModel> resultRoomModel = roomTransformer.transformToOptionalRoomModel(OPTIONAL_ROOM);

        verify(modelMapper).map(OPTIONAL_ROOM, ROOM_MODEL_CLASS);
        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isNotEmpty();
        Assertions.assertThat(resultRoomModel).isEqualTo(OPTIONAL_ROOM_MODEL);
    }

    @Test
    public void testTransformToOptionalRoomModelShouldReturnEmptyOptionalOfEmptyRoom(){

        Optional<RoomModel> resultRoomModel = roomTransformer.transformToOptionalRoomModel(EMPTY_ROOM);

        Assertions.assertThat(resultRoomModel).isNotNull();
        Assertions.assertThat(resultRoomModel).isEmpty();
    }
}
