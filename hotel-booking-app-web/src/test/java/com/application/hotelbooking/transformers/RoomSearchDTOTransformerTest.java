package com.application.hotelbooking.transformers;

import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.ReservableRoomViewDTO;
import com.application.hotelbooking.dto.RoomSearchFormDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomSearchDTOTransformerTest {

    private static final RoomSearchFormDTO ROOM_SEARCH_FORM_DTO = new RoomSearchFormDTO();
    private static final Class<RoomSearchFormServiceDTO> ROOM_SEARCH_FORM_SERVICE_DTO_CLASS = RoomSearchFormServiceDTO.class;
    private static final RoomSearchFormServiceDTO ROOM_SEARCH_FORM_SERVICE_DTO = new RoomSearchFormServiceDTO();
    private static final Class<ReservableRoomDTO> RESERVABLE_ROOM_DTO_CLASS = ReservableRoomDTO.class;
    private static final ReservableRoomDTO RESERVABLE_ROOM_DTO = new ReservableRoomDTO();
    private static final List<ReservableRoomDTO> RESERVABLE_ROOM_DTO_LIST = List.of(RESERVABLE_ROOM_DTO);
    private static final Class<ReservableRoomViewDTO> RESERVABLE_ROOM_VIEW_DTO_CLASS = ReservableRoomViewDTO.class;
    private static final ReservableRoomViewDTO RESERVABLE_ROOM_VIEW_DTO = new ReservableRoomViewDTO();
    private static final List<ReservableRoomViewDTO> RESERVABLE_ROOM_VIEW_DTO_LIST = List.of(RESERVABLE_ROOM_VIEW_DTO);

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

    @Test
    public void testTransformToReservationModelShouldReturnTransformedReservationView(){
        when(modelMapper.map(RESERVABLE_ROOM_VIEW_DTO, RESERVABLE_ROOM_DTO_CLASS)).thenReturn(RESERVABLE_ROOM_DTO);

        ReservableRoomDTO resultReservableRoomDTO= roomSearchDTOTransformer.transformToRoomSearchResultDTO(RESERVABLE_ROOM_VIEW_DTO);

        verify(modelMapper).map(RESERVABLE_ROOM_VIEW_DTO, RESERVABLE_ROOM_DTO_CLASS);
        Assertions.assertThat(resultReservableRoomDTO).isNotNull();
        Assertions.assertThat(resultReservableRoomDTO).isEqualTo(RESERVABLE_ROOM_DTO);
    }

    @Test
    public void testTransformToReservationViewsShouldReturnListOfTransformedReservationModels(){
        when(modelMapper.map(RESERVABLE_ROOM_DTO, RESERVABLE_ROOM_VIEW_DTO_CLASS)).thenReturn(RESERVABLE_ROOM_VIEW_DTO);

        List<ReservableRoomViewDTO> resultReservableRoomViewDTOs = roomSearchDTOTransformer.transformToRoomSearchResultViewDTOs(RESERVABLE_ROOM_DTO_LIST);

        verify(modelMapper).map(RESERVABLE_ROOM_DTO, RESERVABLE_ROOM_VIEW_DTO_CLASS);
        Assertions.assertThat(resultReservableRoomViewDTOs).isNotNull();
        Assertions.assertThat(resultReservableRoomViewDTOs).isNotEmpty();
        Assertions.assertThat(resultReservableRoomViewDTOs).isEqualTo(RESERVABLE_ROOM_VIEW_DTO_LIST);
    }
}
