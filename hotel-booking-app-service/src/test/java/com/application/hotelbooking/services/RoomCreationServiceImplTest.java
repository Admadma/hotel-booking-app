package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.dto.RoomCreationServiceDTO;
import com.application.hotelbooking.services.implementations.RoomCreationServiceImpl;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomCreationServiceImplTest {

    public static final RoomCreationServiceDTO roomCreationServiceDTO = RoomCreationServiceDTO.builder().hotelId(1l).build();
    public static final RoomModel roomModel = RoomModel.builder().build();

    @InjectMocks
    private RoomCreationServiceImpl roomCreationService;

    @Mock
    private RoomRepositoryService roomRepositoryService;

    @Mock
    private HotelService hotelService;

    @Test
    public void testCreateRoomFromDTOShouldReturnRoomModelWithIncreasedRoomNumberIfNoErrors(){
        when(hotelService.getLatestRoomNumberOfHotel(roomCreationServiceDTO.getHotelId())).thenReturn(42);
        when(roomRepositoryService.saveRoom(roomCreationServiceDTO)).thenReturn(roomModel);

        RoomModel resultRoomModel = roomCreationService.createRoomFromDTO(roomCreationServiceDTO);

        verify(hotelService).getLatestRoomNumberOfHotel(roomCreationServiceDTO.getHotelId());
        verify(roomRepositoryService).saveRoom(roomCreationServiceDTO);
        Assertions.assertThat(resultRoomModel.getRoomNumber()).isEqualTo(roomModel.getRoomNumber());
    }

    @Test
    public void testCreateRoomFromDTOShouldThrowExceptionIfJPARepositorySaveMethodThrowsDataAccessException(){
        when(hotelService.getLatestRoomNumberOfHotel(roomCreationServiceDTO.getHotelId())).thenReturn(42);
        when(roomRepositoryService.saveRoom(roomCreationServiceDTO)).thenThrow(DuplicateKeyException.class);

        Assertions.assertThatThrownBy(() -> roomCreationService.createRoomFromDTO(roomCreationServiceDTO))
                .isInstanceOf(DataAccessException.class);

        verify(hotelService).getLatestRoomNumberOfHotel(roomCreationServiceDTO.getHotelId());
        verify(roomRepositoryService).saveRoom(roomCreationServiceDTO);
    }
}
