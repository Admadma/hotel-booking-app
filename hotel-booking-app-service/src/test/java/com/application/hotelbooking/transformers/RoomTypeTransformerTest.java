package com.application.hotelbooking.transformers;

import com.application.hotelbooking.entities.Room;
import com.application.hotelbooking.entities.RoomType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomTypeTransformerTest {

    private static final com.application.hotelbooking.domain.RoomType FAMILY_ROOM_DOMAIN = com.application.hotelbooking.domain.RoomType.FAMILY_ROOM;
    private static final com.application.hotelbooking.entities.RoomType FAMILY_ROOM_ENTITY = com.application.hotelbooking.entities.RoomType.FAMILY_ROOM;
    private static final Class<com.application.hotelbooking.entities.RoomType> ROOM_TYPE_ENTITY_CLASS = com.application.hotelbooking.entities.RoomType.class;

    @InjectMocks
    private RoomTypeTransformer roomTypeTransformer;

    @Mock
    private ModelMapper modelMapper;
    @Test
    public void testTransformToRoomTypeEntityShouldReturnNullIfParameterIsNull(){

        RoomType resultRoomType = roomTypeTransformer.transformToRoomTypeEntity(null);

        Assertions.assertThat(resultRoomType).isNull();
    }

    @Test
    public void testTransformToRoomTypeEntityShouldTransformReceivedRoomTypeToRoomTypeEntityInstance(){
        when(modelMapper.map(FAMILY_ROOM_DOMAIN, ROOM_TYPE_ENTITY_CLASS)).thenReturn(FAMILY_ROOM_ENTITY);

        RoomType resultRoomType = roomTypeTransformer.transformToRoomTypeEntity(FAMILY_ROOM_DOMAIN);

        verify(modelMapper).map(FAMILY_ROOM_DOMAIN, ROOM_TYPE_ENTITY_CLASS);
        Assertions.assertThat(resultRoomType).isNotNull();
        Assertions.assertThat(resultRoomType).isEqualTo(FAMILY_ROOM_ENTITY);
    }
}
