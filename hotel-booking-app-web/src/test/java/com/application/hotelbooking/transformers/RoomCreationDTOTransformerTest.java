package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.HotelView;
import com.application.hotelbooking.dto.HotelCreationServiceDTO;
import com.application.hotelbooking.dto.RoomCreationDTO;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
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
public class RoomCreationDTOTransformerTest {

    private static final RoomCreationDTO ROOM_CREATION_DTO = new RoomCreationDTO();
    private static final Class<RoomCreationServiceDTO> ROOM_CREATION_SERVICE_DTO_CLASS = RoomCreationServiceDTO.class;
    private static final RoomCreationServiceDTO ROOM_CREATION_SERVICE_DTO = new RoomCreationServiceDTO();

    @InjectMocks
    private RoomCreationDTOTransformer roomCreationDTOTransformer;
    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToRoomCreationServiceDTOShouldReturnTransformedRoomCreationDTO(){
        when(modelMapper.map(ROOM_CREATION_DTO, ROOM_CREATION_SERVICE_DTO_CLASS)).thenReturn(ROOM_CREATION_SERVICE_DTO);

        RoomCreationServiceDTO resultRoomCreationServiceDTO = roomCreationDTOTransformer.transformToRoomCreationServiceDTO(ROOM_CREATION_DTO);

        verify(modelMapper).map(ROOM_CREATION_DTO, ROOM_CREATION_SERVICE_DTO_CLASS);
        Assertions.assertThat(resultRoomCreationServiceDTO).isEqualTo(ROOM_CREATION_SERVICE_DTO);
    }
}
