package com.application.hotelbooking.transformers;

import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
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
public class RoomSearchDTOTransformerTest {

    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO = new RoomSearchFormDTO();
    private static final Class<RoomSearchFormServiceDTO> ROOM_SEARCH_FORM_SERVICE_DTO_CLASS = RoomSearchFormServiceDTO.class;
    private static final RoomSearchFormServiceDTO ROOM_SEARCH_FORM_SERVICE_DTO = new RoomSearchFormServiceDTO();

    @InjectMocks
    private RoomSearchDTOTransformer roomSearchDTOTransformer;
    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testTransformToRoomSearchFormServiceDTOShouldReturnTransformedRoomSearchFormDTO(){
        when(modelMapper.map(ROOM_SEARCH_FORM_DTO, ROOM_SEARCH_FORM_SERVICE_DTO_CLASS)).thenReturn(ROOM_SEARCH_FORM_SERVICE_DTO);

        RoomSearchFormServiceDTO resultRoomSearchFormServiceDTO = roomSearchDTOTransformer.transformToRoomSearchFormServiceDTO(ROOM_SEARCH_FORM_DTO);

        verify(modelMapper).map(ROOM_SEARCH_FORM_DTO, ROOM_SEARCH_FORM_SERVICE_DTO_CLASS);
        Assertions.assertThat(resultRoomSearchFormServiceDTO).isNotNull();
        Assertions.assertThat(resultRoomSearchFormServiceDTO).isEqualTo(ROOM_SEARCH_FORM_SERVICE_DTO);
    }
}
