package com.application.hotelbooking.services;

import com.application.hotelbooking.domain.HotelModel;
import com.application.hotelbooking.domain.RoomModel;
import com.application.hotelbooking.domain.RoomType;
import com.application.hotelbooking.dto.ReservableRoomDTO;
import com.application.hotelbooking.dto.RoomSearchFormServiceDTO;
import com.application.hotelbooking.services.implementations.RoomServiceImpl;
import com.application.hotelbooking.services.repositoryservices.RoomRepositoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    private static final List<Long> ID_ROOMS_WITH_CONDITIONS = List.of(1l, 2l, 3l, 4l, 5l);
    private static final List<Long> ID_AVAILABLE_ROOMS = List.of(1l, 4l);
    private static final RoomSearchFormServiceDTO ROOM_SEARCH_FORM_SERVICE_DTO = RoomSearchFormServiceDTO.builder()
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(5))
            .build();

    private static final HotelModel HOTEL = HotelModel.builder().hotelName("Hotel_1").city("City_1").build();
    private static final Optional<RoomModel> AVAILABLE_ROOM_1 = Optional.of(RoomModel.builder()
            .roomNumber(1)
            .singleBeds(2)
            .doubleBeds(1)
            .roomType(RoomType.FAMILY_ROOM)
            .pricePerNight(100)
            .hotel(HOTEL)
            .build());
    private static final Optional<RoomModel> AVAILABLE_ROOM_2 = Optional.of(RoomModel.builder()
            .roomNumber(4)
            .singleBeds(2)
            .doubleBeds(1)
            .roomType(RoomType.FAMILY_ROOM)
            .pricePerNight(150)
            .hotel(HOTEL)
            .build());



    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepositoryService roomRepositoryService;

    @Mock
    private ReservationService reservationService;

    @Test
    public void testSearchRoomsShouldOnlyReturnRoomsThatMatchSearchConditionsAndAvailableInGivenTimePeriod(){
        when(roomRepositoryService.getRoomsWithConditions(ROOM_SEARCH_FORM_SERVICE_DTO)).thenReturn(ID_ROOMS_WITH_CONDITIONS);
        when(reservationService.filterFreeRooms(ID_ROOMS_WITH_CONDITIONS, ROOM_SEARCH_FORM_SERVICE_DTO.getStartDate(), ROOM_SEARCH_FORM_SERVICE_DTO.getEndDate())).thenReturn(ID_AVAILABLE_ROOMS);
        when(roomRepositoryService.getRoomById(1l)).thenReturn(AVAILABLE_ROOM_1);
        when(roomRepositoryService.getRoomById(4l)).thenReturn(AVAILABLE_ROOM_2);

        List<ReservableRoomDTO> resultReservableRoomDTO = roomService.searchRooms(ROOM_SEARCH_FORM_SERVICE_DTO);

        verify(roomRepositoryService).getRoomsWithConditions(ROOM_SEARCH_FORM_SERVICE_DTO);
        verify(reservationService).filterFreeRooms(ID_ROOMS_WITH_CONDITIONS, ROOM_SEARCH_FORM_SERVICE_DTO.getStartDate(), ROOM_SEARCH_FORM_SERVICE_DTO.getEndDate());
        verify(roomRepositoryService).getRoomById(1l);
        verify(roomRepositoryService).getRoomById(4l);
        Assertions.assertThat(resultReservableRoomDTO.size()).isEqualTo(2);
        Assertions.assertThat(resultReservableRoomDTO.get(0).getRoomNumber()).isEqualTo(1);
        Assertions.assertThat(resultReservableRoomDTO.get(1).getRoomNumber()).isEqualTo(4);
    }
}
